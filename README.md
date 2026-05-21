



# とれたてBOX

## 概要

日本の地域名産の野菜・果物を買えるECサイトです。

  ● 作成した時期：2025.12.01～2026.05.19
  
  ● 所要時間：約120時間

ECサイト制作に興味があり、ポートフォリオ制作を通してECサイト開発を学ぶことを目的に作成しました

また、自分の好きな「食」をテーマに、日本各地の野菜・果物を購入できるECサイトとして制作しました

## 主な機能

- ユーザー登録 / ログイン機能
- 商品一覧表示 / 商品詳細表示
- 商品検索機能
- カート追加・数量変更・削除機能
- 購入機能（ログインユーザー / ゲスト購入対応）
- 管理者による商品・注文・顧客管理機能

## 起動方法

① データベース作成

CREATE DATABASE toretatebox; 

② SQL実行

src/main/resources/sql
- schema.sql（テーブル作成）
- data.sql（初期データ）

③ アプリ起動

http://localhost:8080/ にアクセス

---

## テスト用アカウント

### 一般ユーザー
メールアドレス：bull@example.com  
パスワード：kyabetsu  

### 管理者ユーザー
メールアドレス：okinawa@example.com  
パスワード：umibudou

※ パスワードはBCryptでハッシュ化されています

---


## 画面デモ

### 食材閲覧
トップ画面から商品一覧・詳細ページへの遷移を確認できます





https://github.com/user-attachments/assets/5c5fd6c3-f226-4fb8-b98f-9eef4ffd3669





### カート機能
商品追加・数量変更・削除などのカート操作が可能です





https://github.com/user-attachments/assets/08cebcda-7868-416b-aa02-0f6f8795d66d







### 購入（ログインユーザー）
ログイン済みユーザーによる購入の流れを確認できます






https://github.com/user-attachments/assets/edc48853-30df-421c-bf90-727ea826ea01







### 購入（ゲストユーザー）
ゲストユーザーでも購入できる流れを確認できます




https://github.com/user-attachments/assets/6c5f0524-c3ef-4c22-bc6d-67a7e44c4ecf






  
## 使用技術

- Java (Spring Boot)
- Thymeleaf
- MySQL
- HTML / CSS
- JavaScript
- Bootstrap

## 工夫した点

- 共通CSSを作成し、デザインの統一を行いました
- メッセージファイルを使用し、ラベルやエラーメッセージを一元管理しました
- パスワードをハッシュ化してセキュリティを考慮しました
- ゲストユーザーとログインユーザーで購入処理を分け、異なる購入フローに対応しました

## 苦労した点

- Spring Securityの設定でログイン処理がなかなかうまくいきませんでした 
→ 設定クラスを分けて整理することで解決しました

- 画面ごとのデザインがバラバラになりました
→ 共通CSSを作成して統一しました
