# Songstagram

[Songstagram AWS 배포 링크 ](http://songstagram-env.eba-rhjikqps.us-east-2.elasticbeanstalk.com/)



### 소개

사진과 함께 노래를 추천할 수 있는 음악 SNS 프로젝트입니다.

앨범 커버, 재생중인 음악 화면 또는 해당 곡과 어울리는 사진과 함께 자신이 무슨 노래를 듣는지 다른 사람들에게 추천할 수 있습니다.

음악 취향이 비슷한 사람을 팔로우 하고 좋아요를 누르세요.



------



### 사용 기술

front : HTML & Bootstrap 4

back : Spring boot & Spring Data JPA

DB : RDS MySQL

웹 배포 : AWS Elastic BeansTalk & S3 버킷 & RDS 



------



### Class Diagram

![Class Diagram](https://github.com/chosh95/Songstagram/blob/master/ClassDiagram/ClassDiagram.jpg?raw=true)



------



### 구현 기능

* User 관련
  * 회원 가입
  * 로그인
  * 회원 탈퇴
  * 회원 정보 수정
  * 프로필 사진 설정
  * 프로필 확인 (상단 우측의 프로필 사진 선택)
* Post 관련
  * 게시글 작성 : 사진, 가수, 곡 제목, 글을 자유롭게 적을 수 있다. (상단 글 쓰기 아이콘)
  * 게시글 수정 : 가수, 곡 제목, 글을 수정할 수 있다.
* Follow 관련 
  * 다른 사용자를 팔로우 & 언팔로우 할 수 있다. 
  * 프로필 화면에서 팔로워와 팔로잉 목록을 확인할 수 있다.
  * 팔로잉한 사람의 게시글을 확인할 수 있다. (상단 글 목록 아이콘)
* Like 관련
  * 마음에 드는 게시글에 좋아요를 누르거나 취소할 수 있다.
  * 좋아요 누른 게시글 목록을 확인할 수 있다. (상단 하트 아이콘)
* Comment 관련
  * 게시글에 댓글을 적을 수 있다.
  * 본인이 작성한 댓글을 삭제할 수 있다.
* Interceptor 관련 (20.08.05 업데이트)
  * 다른 사용자의 댓글, 게시글을 삭제 & 수정할 수 없습니다.
  * 다른 사용자의 정보를 수정하거나 탈퇴할 수 없습니다.
  * 이미 팔로우한 사용자를 다시 팔로우할 수 없습니다.

------



### 사용 예시



##### index 페이지 화면 : 최신 게시글을 확인할 수 있다.

<img src="https://github.com/chosh95/Songstagram/blob/master/Image/index.png?raw=true" alt="index" style="zoom:50%;" />



##### 개인 프로필 화면 : 게시글, 팔로워, 팔로잉 수와 목록, 그리고 개인이 작성한 글 목록을 확인할 수 있다.

<img src="https://github.com/chosh95/Songstagram/blob/master/Image/profile.png?raw=true" style="zoom:50%;" />

##### 게시글 확인 : 게시글을 자세히 볼 수 있다. 곡명, 글, 사진, 댓글, 좋아요 등을 확인할 수 있다.

![](https://github.com/chosh95/Songstagram/blob/master/Image/read.png?raw=true)





##### 팔로우 목록 확인 : 내가 팔로우 한 사람의 게시글을 확인할 수 있다.

![](https://github.com/chosh95/Songstagram/blob/master/Image/follow.png?raw=true)



##### 좋아요 목록 : 좋아요한 게시글의 목록을 확인할 수 있다.

![](https://github.com/chosh95/Songstagram/blob/master/Image/like.png?raw=true)

------



### 개발 기간

2020.07.06 ~ 

꾸준히 발전시키는 중

