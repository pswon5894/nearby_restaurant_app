네이버지도 api
https://navermaps.github.io/android-map-sdk/guide-ko/1.html

네이버 지도 401 에러 클라이언트 아이디 관련
로컬 프로퍼티 ?
빌드 그래들 ?
Authorization failed: [401] Unauthorized client

공식문서대로 manifest 수정
AndroidManifest.xml에 지정
AndroidManifest.xml의 <meta-data>로 클라이언트 ID를 지정할 수 있습니다. <application> 아래에 <meta-data> 요소를 추가하고, name으로 com.naver.maps.map.NCP_KEY_ID를, value로 발급받은 클라이언트 ID를 지정합니다.

다음은 AndroidManifext.xml에 클라이언트 ID를 지정하는 예제입니다.

<manifest>
    <application>
        <meta-data
            android:name="com.naver.maps.map.NCP_KEY_ID"
            android:value="YOUR_NCP_KEY_ID_HERE" />
    </application>
</manifest>
https://navermaps.github.io/android-map-sdk/guide-ko/1.html
//네이버 맵 가이드

카카오지도 1일 30만회, 네이버 지도 1일 60만회 대표개정 무료
