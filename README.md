# Mall API Demo

電商後端學習Project。

## 目的

學習如何使用spring boot開發WEB API，與Unit test學習。

## 專案主要資料夾結構

#### Controller

- 後端路由控制

#### Services

- 主要業務邏輯

#### Dao

- 資料庫數據處理

## Web API 設計

#### 取得 API 服務資料示範 URL

Example:`http://localhost/endpoint`

![image](https://github.com/christu6899/springboot-mall/blob/main/API%20DOC.png
)


## 資料庫

### 資料庫連接設定

- 資料庫使用Mysql，透過spring jdbc撰寫純SQL Query操作資料庫中的數據。

## 部屬

### Local部屬

下載docker compose中的三個檔案，並放在同個資料夾，確認電腦中有安裝docker compose。
執行以下指令:
`docker compose up -d`