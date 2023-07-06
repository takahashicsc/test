# POE-SHINOKEN-REAAS-APP-Android
POE シノケン REAAS APP Androidプロジェクト

================

### Target
- minSdkVersion 23
- targetSdkVersion 30
- compileSdkVersion 30

## Technology and Architecture

### ツール、言語バージョン

|Tools| Version|
----|---- 
|AndroidStudio|4.1 |
| Gradle | 4.1 |
| Kotlin | 1.4.10 |

### ライブラリ

- Groupie  
RecyclerViewライブラリ  

- Lottie  
AfterEffects表示用ライブラリ

- Coil  
画像ロード用ライブラリ

- Loupe  
画像詳細用のViewライブラリ

### アーキテクチャ
Android Architecture Components

### ディレクトリ構成
```
app
  │
  ├── api --- API
  │
  ├── di --- Dagger Android Hilt
  │
  ├── extensions --- Extensions
  │
  ├── model --- Models
  │
  ├── repository --- repository
  │    │
  │    ├── imple --- repository実装クラス
  │
  ├── database
  │
  ├── ui --- Activity, Fragment, Widget
  │    │
  │    ├── activity
  │    │
  │    ├── screen --- Fragment + ViewModel
  │    │
  │    └── widget
  │
  └── utils

  ```

### ビルド手順
1. Android Studio を開く
2. 必要に応じてアップデートして下さい
3. Open -> Project -> POE-SHINOKEN-REAAS-APP-Android -> Shinoken
4. Build Variants からビルドタイプを選択 (AndroidStudio左にBuild Variantsを表示させるタブがあります)
5. ▶︎ にてビルド

### ビルドタイプ
| BuildType | API接続先
----|----
| stub | なし |
| develop | https://shinoken.reivo.info/api/v1/ |
| staging | https://staging.res-api.reaas.tech/api/v1/ |
| production | https://res-api.reaas.tech/api/v1/ |
