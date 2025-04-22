import numpy as np
from datetime import datetime, timedelta
import sys
import os
# 时间起点
START_TIME = datetime(2024, 12, 29, 0, 0, 0)
INTERVAL_MINUTES = 10
TOTAL_POINTS = 144 * 3  # 432

def get_time_index(target_time: datetime) -> int:
    """获取目标时间所对应的时间段前一个10分钟区间的索引"""
    delta = target_time - START_TIME
    total_minutes = int(delta.total_seconds() // 60)
    index = total_minutes // INTERVAL_MINUTES - 1  # 取前一个时间段
    return max(0, min(index, TOTAL_POINTS - 1))

def get_avg_speed_for_time(time_str: str):
    # 解析输入时间
    target_time = datetime.strptime(time_str, "%Y/%m/%d %H:%M:%S")

    # 加载数据：形状应为 (89, 432)
    base_path = os.path.dirname(os.path.abspath(__file__))  # 获取当前脚本目录

    data  = np.load(os.path.join(base_path, "average_speedAll.npy"))

    if data.shape != (89, TOTAL_POINTS):
        raise ValueError(f"数据维度不匹配，当前数据形状为 {data.shape}，应为 (89, 432)")

    # 获取时间索引
    index = get_time_index(target_time)

    # 对89行数据该时段取平均
    avg_speed = data[:, index].mean()

    print(f"{avg_speed:.2f}")

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("用法: python averageSpeedCount.py '2024-12-30 10:05:05'")
    else:
        get_avg_speed_for_time(sys.argv[1])
