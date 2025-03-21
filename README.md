## 프로젝트 실행 가이드
#### 실행 방법
1. WAS프로젝트에서 mvn clean install 실행 (APP프로젝트에 pom.xml에서 사용)
2. APP프로젝트에서 SimpleApplicationContext.java에서 실행

#### 서블릿 등록 방법
1. 서블릿 클래스 위치는 SimpleApplicationContext 이하에 존재해야됩니다.
2. 서블릿 클래스에 SimpleServlet인터페이스를 implements
3. src/main/resources/META-INF/services/org.servlet.SimpleServlet 파일에 클래스 등록

