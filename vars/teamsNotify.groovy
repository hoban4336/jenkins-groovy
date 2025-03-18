def call(code,appName) {
    // Teams webhook URL (Credential로 관리하는 것이 좋습니다.)
    def webhookUrl = 'https://prod-94.southeastasia.logic.azure.com:443/workflows/437ea9546175408f9f1b1559963b8548/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=H1jLbtNtINn9EiTINaHq7i7T0yqgwGp5sUBkLxcjZYc'

    def dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    def endTime = dateFormat.format(new Date(currentBuild.timeInMillis))
    
    def payload = """
{
  "attachments": [
    {
      "contentType": "application/vnd.microsoft.card.adaptive",
      "contentUrl": null,
      "content": {
        "$schema": "http://adaptivecards.io/schemas/adaptive-card.json",
        "type": "AdaptiveCard",
        "version": "1.4",
        "summary": "Jenkins Notification",
        "body": [
          {
            "type": "TextBlock",
            "size": "Medium",
            "weight": "Bolder",
            "text": "젠킨스에서 ${appName} 빌드가 ${code>0?'성공':'실패'} 했습니다."
          },
          {
            "type": "TextBlock",
            "spacing": "None",
            "text": "${appName} v${BUILD_NUMBER}__${endTime}",
            "isSubtle": true,
            "wrap": true
          },
          {
            "type": "FactSet",
            "facts": [
              { "title": "빌드 요청", "value": "${user}" },
              { "title": "빌드 시간", "value": "${endTime}" },
              { "title": "빌드 넘버", "value": "v${BUILD_NUMBER}" },
              { "title": "Repo 위치", "value": "${env.GIT_URL}" },
              { "title": "Git Push User", "value": "${env.gitlabUserUsername}" },
              { "title": "Git Branch Name", "value": "${env.GIT_BRANCH}" },
              { "title": "Build Result", "value": "${env.LOG_RESULT_DOCKER}" },
              { "title": "Jenkins Console", "value": "[Link](${BUILD_URL}console)" }
            ]
          }
        ]
      }
    }
  ]
}
    """

    def response = httpRequest(
        httpMode: 'POST',
        contentType: 'APPLICATION_JSON',
        requestBody: payload,
        url: webhookUrl
    )
    
    echo "Response Status: ${response.status}"
}

