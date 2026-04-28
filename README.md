



# とれたてBOX

## 概要

日本の地域名産の野菜・果物を買えるECサイトです。

  ● 作成した時期：2025.12.01～2026.03.09
  
  ● 所要時間：約120時間

自分がECサイトの制作の仕方に興味があり、ポートフォリオを作成しながらECサイト作成の学習もできればと思い自分の好きな食についてのテーマで制作しました。

## 主な機能

- ユーザー登録 / ログイン
- 商品一覧表示
- 商品詳細ページ
- 商品検索
- カート機能
- 購入機能
- 管理者による管理機能(食材、注文、顧客)

## 起動方法

① データベース作成

CREATE DATABASE toretatebox;

② application.properties 設定

spring.datasource.url=jdbc:mysql://localhost:3306/toretatebox  
spring.datasource.username=root  
spring.datasource.password=password  

③ SQL実行

- schema.sql（テーブル作成）
- data.sql（初期データ）

④ アプリ起動

http://localhost:8080 にアクセス

---

## テスト用アカウント

### 一般ユーザー
メールアドレス：bull@example.com  
パスワード：kyabetsu  

### 管理者ユーザー
メールアドレス：okinawa@example.com  
パスワード：umibudou

※ パスワードはBCryptでハッシュ化されています。

---


## 画面デモ

### 食材閲覧
トップ画面から商品一覧・詳細ページへの遷移を確認できます。

https://github.com/user-attachments/assets/0c3cbd93-3e98-470e-9e31-578540711ca7



### カート機能
商品追加・数量変更・削除などのカート操作が可能です。

https://github.com/user-attachments/assets/8bf317cc-cba6-4341-9249-e3064228a4df



### 購入（ログインユーザー）
ログイン済みユーザーによる購入の流れを確認できます。




https://github.com/user-attachments/assets/ffe95de6-9595-492c-b32b-b535def7a833



### 購入（ゲストユーザー）
ゲストユーザーでも購入できる流れを確認できます。


https://github.com/user-attachments/assets/1aba387b-4a99-4602-b526-7a54ffd04c0f






## データベース設定

以下のSQLを使用してください。

- テーブル作成：src/main/resources/sql/schema.sql
- 初期データ：src/main/resources/sql/data.sql
  
## 使用技術

- Java (Spring Boot)
- Thymeleaf
- MySQL
- HTML / CSS
- JavaScript
- Bootstrap

## 工夫した点

- 共通CSSを作成し、デザインの統一を行いました
- パスワードをハッシュ化してセキュリティを考慮しました
- カート機能でゲストユーザー、ログインユーザーによって動作を分けました

## 苦労した点

- Spring Securityの設定でログイン処理がうまくいかなかった  
→ 設定クラスを分けて整理することで解決しました

- 画面ごとのデザインがバラバラになった  
→ 共通CSSを作成して統一しました
