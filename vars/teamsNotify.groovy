def call(String message) {
    // Teams webhook URL (Credential로 관리하는 것이 좋습니다.)
    def webhookUrl = 'https://outlook.office.com/webhook/...'

    def payload = """
    {
        "@type": "MessageCard",
        "@context": "http://schema.org/extensions",
        "summary": "Jenkins Notification",
        "themeColor": "0076D7",
        "sections": [{
            "activityTitle": "Jenkins 빌드 알림",
            "text": "${message}"
        }]
    }
    """

    httpRequest(
        httpMode: 'POST',
        contentType: 'APPLICATION_JSON',
        requestBody: payload,
        url: webhookUrl
    )
}

