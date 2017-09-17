# k8s-spring-boot

## local

```sh
./gradlew bootRun
# curl localhost:8080/hello
```

## Setup

### gcloud project

- create Google Cloud Platform project `k8s-trial`
- [enable apis](https://console.cloud.google.com/apis/library)
  - Google Compute Engine
  - Google Container Engine
  - Google Cloud Builders
- setup terminal config with following commands

```sh
gcloud config set project k8s-trial
gcloud config set compute/zone asia-northeast1-a
gcloud auth login
```

### build docker image

```sh
gcloud container builds submit --config cloudbuild.yml
# gcloud container builds list
```

## deploy

## create

```sh
# create cluster
gcloud container clusters create k8s-spring-boot --machine-type=f1-micro
# setup kubectl command config
gcloud container clusters get-credentials k8s-spring-boot --zone asia-northeast1-a --project k8s-trial
# confirm config
kubectl config current-context
kubectl create -f deployment.yml
kubectl create -f service.yml
kubectl get service -w # get external ip, curl external_ip/hello
```

## update

```sh
# create new docker image
# edit deployment.yml docker image
kubectl apply -f deployment.yml --record
```

## scaleout

```sh
kubectl scale deployment k8s-spring-boot-deployment --replicas 4
```

## Remove

```sh
kubectl delete service k8s-spring-boot-service
kubectl delete service k8s-spring-boot-service
```

# Google Cloud SQL

container <-> Cloud SQL Proxy <-> Cloud SQL

- [enable apis](https://console.cloud.google.com/apis/library)
  - Cloud SQL Administration API
- [create service account](https://console.cloud.google.com/iam-admin/serviceaccounts/)
  - and download cf as `cloudsql_serviceaccount.json`
- create cloudsql instance & user
```sh
gcloud sql instances create gke-cloudsql --tier db-f1-micro --region=asia-east1
gcloud sql users create proxyuser cloudsqlproxy~% --instance=gke-cloudsql
```

- create cloudsql instance access secret
```
kubectl create secret generic cloudsql-instance-credentials --from-file=credentials.json=cloudsql_serviceaccount.json
```

- create cloudsql database access secret
```
kubectl create secret generic cloudsql-db-credentials --from-literal=username=cloudsqlproxy
```

then deploy, and get `curl localhost:8080/jdbc`

## Logging

- https://github.com/wercker/stern

```sh
stern k8s-spring-boot
```
