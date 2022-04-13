# Todo Application
Todo List를 통한 효율적인 todo 작성 및 시간 관리

## 요구사항
- TodoEntity (id/title/content/complete/dueTime) 생성
- 여러 Table를 통한 체계적인 TodoEntity 관리
- RecyclerView를 통한 item touch handling 구현(삭제, 이동 가능)
- table 생성 및 삭제 가능


---

<br>

## Recycler View란?
- ListView는 이제 쓰이지 않는 추세라고 한다...RecyclerView 공부하자
- touch Handling 쉽게 구현 가능
- 효율적인 자원 관리
- https://velog.io/@eogns0321/Recycler-View-%EA%B0%84%EB%8B%A8-%EC%A0%95%EB%A6%AC - RecyclerView 정리 내용


---

<br>

## SQLite DataBase
로컬 기기에 todo List 정보를 저장하고 이를 불러오는 기능을 구현하기 위해 <br>
안드로이드 스튜디오에서 지원하는 SQLiteOpenHelper를 사용했다. <br>
SQLiteOpenHelper를 통해 database를 안전하게 관리하고 버젼 업데이트가 가능하다.<br>
또한 DBHandler라는 class를 통해 효과적인 item insert,delete,update가 가능했다.<br>

<br>
이때 DB에서 data 읽어오는 Thread가 Main Thread가 아닌 IO Thread에서 실행되므로 비동기 이슈가 있었다. <br>
CoroutineScope를 사용하여 비동기 처리를 통해 해결했다.

<br>

---

<br>



## 화면
![image](https://user-images.githubusercontent.com/50730897/163180018-18ada6a5-b356-49f3-8072-ffa3302c4606.png)

## 영상
https://user-images.githubusercontent.com/50730897/163180495-d346648f-515a-4106-b909-532236bf8ac9.mp4


<br>

---

<br>

## 기술 스택
Android Studio(Kotlin), SQLite
