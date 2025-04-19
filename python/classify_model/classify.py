##核K聚类（西站交通数据）预测29号一天
import numpy as np
from sklearn.preprocessing import StandardScaler
import json
def rbf_kernel(X, gamma=None):
    """计算RBF核矩阵"""
    if gamma is None:
        gamma = 1.0 / X.shape[1]
    sq_dists = np.sum(X**2, axis=1).reshape(-1, 1) + np.sum(X**2, axis=1) - 2 * np.dot(X, X.T)
    K = np.exp(-gamma * sq_dists)
    return K

def kernel_kmeans(K, n_clusters, max_iter=100):
    """核K均值算法实现"""
    n_samples = K.shape[0]
    
    # 初始化标签并确保每个簇至少有一个样本
    while True:
        labels = np.random.randint(0, n_clusters, n_samples)
        if len(np.unique(labels)) == n_clusters:
            break
    
    for _ in range(max_iter):
        # 计算每个样本到各簇的距离
        distances = np.zeros((n_samples, n_clusters))
        for k in range(n_clusters):
            mask = (labels == k)
            m_k = np.sum(mask)
            
            if m_k == 0:
                continue
            
            # 计算距离分量
            sum_K_ij = K[:, mask].sum(axis=1)
            sum_K_jl = K[mask][:, mask].sum()
            
            distances[:, k] = np.diag(K) - (2 * sum_K_ij / m_k) + (sum_K_jl / (m_k ** 2))
        
        # 分配新标签
        new_labels = np.argmin(distances, axis=1)
        
        # 处理空簇
        unique_labels = np.unique(new_labels)
        if len(unique_labels) < n_clusters:
            missing = set(range(n_clusters)) - set(unique_labels)
            for m in missing:
                random_sample = np.random.choice(n_samples)
                new_labels[random_sample] = m
        
        # 检查收敛
        if np.all(labels == new_labels):
            break
        labels = new_labels.copy()
    
    return labels

def main(data):
    # 数据标准化
    scaler = StandardScaler()
    data_scaled = scaler.fit_transform(data)
    
    # 计算核矩阵
    K = rbf_kernel(data_scaled, gamma=0.5)
    
    # 执行核K均值聚类
    labels = kernel_kmeans(K, n_clusters=3)
    
    # 根据车速确定簇标签含义
    cluster_speeds = [data[labels == k, 2].mean() for k in range(3)]
    speed_order = np.argsort(cluster_speeds)[::-1]  # 从高到低排序
    
    state_mapping = {
        speed_order[0]: 0,
        speed_order[1]: 1,
        speed_order[2]: 2
    }
    
    # 转换预测标签
    predicted_states = [state_mapping[label] for label in labels]
    
    # 输出结果
    return predicted_states#聚类结果

#输入，从文件夹导入（此时的数据没有行和列标题）
myspeeds29=np.load('myspeeds29.npy')
mytraFlows29=np.load('mytraFlows29.npy')
max_occupancy=np.load('max_occupancy.npy')
mystates=[]

#流量、占有率、车速
for i in range(myspeeds29.shape[1]):
    traFlows=mytraFlows29[:,i][:,np.newaxis]
    occupancy=traFlows/max_occupancy
    speeds=myspeeds29[:,i][:,np.newaxis]
    data=np.concatenate((traFlows,occupancy,speeds),axis=1)
    predicted_states=main(data)
    mystates.append(predicted_states)

#保存结果
states=np.array(mystates).T
for i in range(states.shape[0]):
    for j in range(states.shape[1]):
        if myspeeds29[i,j]==0 and mytraFlows29[i,j]==0:
            states[i,j]=0
np.save('result.npy',states)
states = np.array(mystates).T  # (路段数, 时间段数)

# 后端打印用
# 避免原始为0时出现噪声状态
for i in range(states.shape[0]):
    for j in range(states.shape[1]):
        if myspeeds29[i, j] == 0 and mytraFlows29[i, j] == 0:
            states[i, j] = 0

# 打印 JSON 数组，每个路段一个状态列表
print(json.dumps(states.tolist()))