def call(code,appName) {
    // Teams webhook URL (Credential로 관리하는 것이 좋습니다.)
    def webhookUrl = 'https://prod-76.southeastasia.logic.azure.com:443/workflows/fdac51af9f5745dc83521ca31e2abaf4/triggers/manual/paths/invoke?api-version=2016-06-01&sp=%2Ftriggers%2Fmanual%2Frun&sv=1.0&sig=t30zVayptl6g1xTMH9jEbDa2ye0C5qok7z5PPGqIEcA'

    def dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    def endTime = dateFormat.format(new Date(currentBuild.timeInMillis))
    
    def payload = """
    {
        "@type": "MessageCard",
        "@context": "http://schema.org/extensions",
        "summary": "Jenkins Notification",
        "themeColor": "0076D7",
        "sections": [{
            "activityTitle": "젠킨스에서 ${appName} 빌드가 ${code>0?'성공':'실패'} 했습니다.",
            "activitySubtitle": "${appName}v${BUILD_NUMBER}__${endTime}",
            "activityImage": "https://adaptivecards.io/content/cats/3.png",
            "facts": [{
                "name": "빌드 요청",
                "value": "${user}"
            }, {
                "name": "빌드 시간",
                "value": "${endTime}"
            }, {
                "name": "빌드 넘버",
                "value": "v${BUILD_NUMBER}"
            },{
                "name": "Repo 위치",
                "value": "${env.GIT_URL}"
            },{
                "name": "Git Push User",
                "value": "${env.gitlabUserUsername}"
            },{
                "name": "Git Branch Name",
                "value": "${env.GIT_BRANCH}"
            },
            {
                "name":"Build Result",
                "value": "<pre>${env.LOG_RESULT_DOCKER}</pre>"
            },
            {
                "name":"Jenkins Console",
                "value": "${BUILD_URL}console"
            }
            ],
            "markdown": true
        }]
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

