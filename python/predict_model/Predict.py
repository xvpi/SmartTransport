import pickle as pk
import numpy as np
import matplotlib.pyplot as plt
import torch
import torch.nn as nn
from scipy.io import loadmat
from stgcn import STGCN
from utils import generate_dataset, load_metr_la_data, get_normalized_adj



num_timesteps_input = 12
num_timesteps_output = 3

A = np.load("A.npy")
X = np.load("X.npy")
X = X[:,np.newaxis]

X = X.astype(np.float32)
A = A.astype(np.float32)

means = np.mean(X, axis=(0, 2))
X = X - means.reshape(1, -1, 1)
stds = np.std(X, axis=(0, 2))
X = X / stds.reshape(1, -1, 1)

A_wave = get_normalized_adj(A)
A_wave = torch.from_numpy(A_wave)
A_wave = A_wave.to(device='cpu')

net = STGCN(A_wave.shape[0],X.shape[1],num_timesteps_input,num_timesteps_output).to(device='cpu')

net.load_state_dict(torch.load('net.pth', weights_only=True))

X, y = generate_dataset(X,num_timesteps_input=num_timesteps_input,num_timesteps_output=num_timesteps_output)
predict = (net(A_wave,X).detach().numpy()*stds+means).astype(np.int64)
future = predict[-1,:,:]
print(future.tolist())  # future 是预测结果的列表



