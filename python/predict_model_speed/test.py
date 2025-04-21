#导入所需的包
import numpy as np

#导入npy文件路径位置
test = np.load('X.npy')
print("数组形状:", test.shape)  # 数组的维度结构
print("数组数据类型:", test.dtype)  # 数组元素的数据类型
print("数组维度:", test.ndim)  # 数组的维度数
print(test)

