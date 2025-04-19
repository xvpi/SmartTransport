import pickle as pk
import numpy as np
import matplotlib.pyplot as plt
import torch
import torch.nn as nn
from scipy.io import loadmat
from stgcn import STGCN
from utils import generate_dataset, load_metr_la_data, get_normalized_adj

use_gpu = False
num_timesteps_input = 12
num_timesteps_output = 3

epochs = 50
batch_size = 50


def train_epoch(training_input, training_target, batch_size):
    
    permutation = torch.randperm(training_input.shape[0])

    epoch_training_losses = []
    for i in range(0, training_input.shape[0], batch_size):
        net.train()
        optimizer.zero_grad()

        indices = permutation[i:i + batch_size]
        X_batch, y_batch = training_input[indices], training_target[indices]
        X_batch = X_batch.to(device='cpu')
        y_batch = y_batch.to(device='cpu')

        out = net(A_wave, X_batch)
        loss = loss_criterion(out, y_batch)
        loss.backward()
        optimizer.step()
        epoch_training_losses.append(loss.detach().cpu().numpy())
    return sum(epoch_training_losses)/len(epoch_training_losses)

torch.manual_seed(7)

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


training_input, training_target = generate_dataset(X,
                                                   num_timesteps_input=num_timesteps_input,
                                                   num_timesteps_output=num_timesteps_output)


A_wave = get_normalized_adj(A)
A_wave = torch.from_numpy(A_wave)

A_wave = A_wave.to(device='cpu')

net = STGCN(A_wave.shape[0],
            training_input.shape[3],
            num_timesteps_input,
            num_timesteps_output).to(device='cpu')

optimizer = torch.optim.Adam(net.parameters(), lr=1e-3)
loss_criterion = nn.MSELoss()

for epoch in range(epochs):
    if((epoch+1)%10==0):
        print("epoch=",epoch+1)
    loss = train_epoch(training_input, training_target,batch_size=batch_size)

