import sys
import numpy as np
import torch
import os
import torch.nn as nn
from datetime import datetime, timedelta
from stgcn import STGCN
from utils import generate_dataset, get_normalized_adj

# ====================== 接收并解析预测时间参数 ======================
if len(sys.argv) < 2:
    print("缺少预测时间参数，格式应为 yyyy/MM/dd HH:mm:ss")
    sys.exit(1)

try:
    predict_time_str = sys.argv[1]
    predict_time = datetime.strptime(predict_time_str, "%Y/%m/%d %H:%M:%S")
except ValueError:
    print("时间格式错误，应为 yyyy/MM/dd HH:mm:ss")
    sys.exit(1)

# ====================== 加载数据 ======================
base_path = os.path.dirname(os.path.abspath(__file__))  # 获取当前脚本目录

A = np.load(os.path.join(base_path, "A.npy"))
X = np.load(os.path.join(base_path, "X.npy"))

# 每10分钟一个点，一天144个点，假设数据从某个已知时间起
start_time = datetime(2024, 12, 29, 0, 0, 0)
delta_minutes = int((predict_time - start_time).total_seconds() / 60)

if delta_minutes % 10 != 0:
    print("预测时间不是10分钟的倍数，请调整")
    sys.exit(1)

col_index = delta_minutes // 10  # 在 X 中的列索引

if col_index < 12:
    print("历史数据不足12个时间步用于预测")
    sys.exit(1)

# ====================== 数据预处理 ======================
X = X[:, np.newaxis, :col_index]  # 截取到预测时间前一刻，不包含当前预测时间
X = X.astype(np.float32)
A = A.astype(np.float32)

means = np.mean(X, axis=(0, 2))
X = X - means.reshape(1, -1, 1)
stds = np.std(X, axis=(0, 2))
X = X / stds.reshape(1, -1, 1)

# ====================== 模型预测 ======================
A_wave = get_normalized_adj(A)
A_wave = torch.from_numpy(A_wave).to(device='cpu')
net = STGCN(A_wave.shape[0], X.shape[1], num_timesteps_input=12, num_timesteps_output=3).to(device='cpu')

net.load_state_dict(torch.load(os.path.join(base_path, "net.pth"), weights_only=True))

X_seq, _ = generate_dataset(X, num_timesteps_input=12, num_timesteps_output=3)
predict = (net(A_wave, X_seq).detach().numpy() * stds + means).astype(np.int64)
future = predict[-1, :, :]  # shape: (89, 3)
print(future.tolist())
