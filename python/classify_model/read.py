#导入所需的包
import numpy as np
import pandas as pd
#导入npy文件路径位置
d1 = np.load('average_speed29.npy')
d2 = np.load('mytraFLows30.npy')
d3 = np.load('mytraFLows31.npy')
combined = np.concatenate([d1, d2, d3], axis=1)
np.save("average_speedAll.npy", combined)

test = np.load('average_speedAll.npy')
# np_to_csv = pd.DataFrame(data=test)
# np_to_csv.to_csv('traflow29.csv', index=False)
print("数组形状:", test.shape)  # 数组的维度结构
print("数组数据类型:", test.dtype)  # 数组元素的数据类型
print("数组维度:", test.ndim)  # 数组的维度数
print(test[:5,:2])