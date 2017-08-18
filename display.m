A = csvread('data/demo.csv');
A = A(1:10000,:);
scatter3(A(:,1), A(:,2), A(:,3), '.');