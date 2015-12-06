#채팅 프로토콜
## 클라이언트가 방에 들어간 후
- 헤더 8 byte와 content로 이뤄진다.
  - 4byte: type 나타내는 integer (0: Message, 1: File)
  - 4byte: content length
  - content length 길이의 byte: 메세지 내용이나, file의 binary array


# 클라이언트 규칙
- ".bye" 를 입력하면 방에서 나가고 프로그램이 종료된다. 이 메세지는 서버에 보내지지 않는다.
- ".file [FILE_NAME]" 을 입력하면 파일을 방에 있는 사람들에게 보낸다.
- 이외의 다른 메세지는 일반 채팅 메세지로 간주되어 닉네임과 함께 전해진다.
