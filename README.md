## Recorder
녹음기 앱

## 사용한 요소
- UI
    - Custom view
    - onSizeChanged
    - onDraw
    - Paint
    - Shape drawable
    - View Attribute
- Media
    - MediaRecorder
    - MediaPlayer
- Storage
    - CacheDir
    
## Custom view
생성자로 Context 와 AttributeSet 를 전달받아 상속할 뷰에 전달한다.
```kotlin
class CountUpTextView(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs)
```
- AppCompat, 이전 버전을 지원하기 위해 맵핑을 도와주는 라이브러리
- xml 에서는 자동으로 적용해줘서 안적어줘도 됨
- 코드는 자동으로 안돼서 직접해줘야함


## onSizeChanged
```kotlin
override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.drawingWidth = w
        this.drawingHeight = h
}
```
- 뷰의 사이즈가 변하거나 초기화 될 때 호출되는 메소드
- 뷰의 사이즈를 저장할 때 사용

## onDraw
```kotlin
override fun onDraw(canvas: Canvas?) {
    super.onDraw(canvas)
    ...
}
```
- 뷰에서 그림을 직접 그릴 때 사용하는 메소드
- Paint 등을 사용해 속성을 정하고 canvas를 사용해 그림을 그린다.

## Paint
```kotlin
val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
}
```
- ANTI_ALIAS_FLAG : 안티 에일리어싱을 적용
- strokeCap : Paint 의 끝 모양을 결정

## Shape drawable
```xml
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">

    <stroke android:width="2dp"
        android:color="@color/light_gray"/>

    <solid android:color="@color/gray"/>

</shape>
```
- 형태를 표현해야 할 때 사용
- oval : 원형

## View Attribute
android:scaleType : 뷰에 있는 이미지의 적용 방식을 설정
- fitCenter : 중앙에 맞게
- padding 을 사용하면 크기를 조정할 수 있다.

## MediaRecorder
[공식 문서](https://developer.android.com/reference/android/media/MediaRecorder?hl=ko)

[개발자 가이드](https://developer.android.com/guide/topics/media/mediarecorder?hl=ko)

음성을 녹음할 때 사용
```kotlin
val recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordingFilePath)
            prepare()
        }
```
- Audio source : 오디오 소스 설정
- Output format : 저장 형식 설정
- Audio encoder : 오디오 인코더 설정
- Output file : 저장 위치 설정

```kotlin
this.recorder?.start()
```
녹음 시작

```kotlin
this.recorder?.run {
            stop()
            release()
        }
```
녹음 종료
- stop 후 release 를 해야 함

```kotlin
recorder?.maxAmplitude
```
- 녹음 시 최대 음량을 구할 수 있다.

## MediaPlayer
[공식 문서](https://developer.android.com/reference/android/media/MediaPlayer?hl=ko)

[개발자 가이드](https://developer.android.com/guide/topics/media/mediaplayer?hl=ko)

멀티 미디어를 실행할 때 사용
```kotlin
this.player = MediaPlayer().apply {
            setDataSource(recordingFilePath)
            prepare()
        }
```
- 사용할 파일 위치를 지정

```kotlin
this.player?.setOnCompletionListener {
            ...
        }
        this.player?.start()
```
- start() 로 시작
- setOnCompletionListener 를 사용하면 종료 됐을 때의 동작 설정 가능

```kotlin
this.player?.release()
```
- release() 로 종료
## CacheDir
[개발 가이드](https://developer.android.com/training/data-storage/app-specific#external-cache-create)

```kotlin
externalCacheDir?.absolutePath
```
- 외부 저장소에 캐시를 생성할 경우 위의 코드로 문자열을 얻을 수 있음
