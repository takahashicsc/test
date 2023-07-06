package jp.co.shinoken.api.aws

enum class CognitoErrorMessages(val title: String, val message: String) {
    NotAuthorizedException(
        title = "ログインに失敗しました",
        message = "メールアドレスまたはパスワードの入力に誤りがあります。\n(E1010)"
    ),
    InvalidParameterException(
        title = "入力内容に誤りがあります",
        message = "入力内容をご確認ください。\n(E1020)"
    ),
    InvalidPasswordException(
        title = "パスワードに誤りがあります",
        message = "パスワードは8文字以上、大文字、小文字、数字、記号が必須になります。\n(E1030)"
    ),
    CodeMismatchException(
        title = "認証コードに誤りがあります",
        message = "再度、メールに届いた認証コードをご確認ください。\n(E1040)"
    ),
    LimitExceededException(
        title = "試行制限を超えました",
        message = "しばらくしてからお試しください。\n(E1050)"
    ),
    CodeExpiredException(
        title = "確認コードの有効期限が切れています",
        message = "「認証コードが届かないとき」から認証コードの再送を行ってください。\n(E1060)"
    ),
    OtherException(
        title = "通信に失敗しました",
        message = "通信環境の良いところで再度お試しください。\n(E1999)"
    )
}