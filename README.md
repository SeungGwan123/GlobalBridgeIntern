# SpringBoot
<pre>
스프링의 핵심 가치 - 애플리케이션 개발에 필요한 기반을 제공해서 개발자가 비즈니스 로직 구현에만 집중 할 수 있게끔 하는 것<br>
제어 역전  - IoC를 적용한 환경에서는 사용할 객체를 직접 생성하지 않고 객체의 생명주기 관리를 외부에 위임합니다.<br>
          - 객체의 관리를 컨테이너에 맡기는 것<br>
          - 의존성 주입(DI : Dependency Injection), 관점 지향 프로그래밍(AOP : Aspect-Oriented Programming)이 가능해집니다.<br>
</pre>

# 의존성 주입(DI : Dependency Injection)
<pre>
생성자, 필드 객체 선언, setter메서드<br>
@RestController - HTTP요청을 처리하고 JSON데이터를 반환하는 역할<br>
                  이게 적용된 클래스의 메서드들은 각각의 엔드포인트에 매핑되어 요청을 처리하고, 데이터를 반환<br>
                  REST API를 제공하기 위한 컨트롤어 역할을 수행, 요청을 처리하고 응답을 반환<br>
@RequiredArgsConstructor - 이 어노테이션은 생성자를 주입시켜주는 역할을 합니다.<br>
                           해당 클래스의 모든 필수 인자를 가진 생성자를 자동으로 생성해주는 기능을 제공합니다.<br>
                           ##주의## final, @NotNull이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션입니다.<br>
ex) @Service<br>
    @RequiredArgsConstructor<br>
    public class BannerServiceImpl implements BannerService {<br>

      private final BannerRepository bannerRepository <br>
@AllArgsConstructor , @Builder , @NoArgsConstructor : 각각 매개변수 전부다초기화, 원하는 만큼 , 매개변수 없이 초기화 가능<br>
ex) @Builder (Lombok에서 제공하는 기능, 빌더패턴 자동으로 생성해줍니다.)<br>
    Person person = Person.builder()<br>
                          .name("John")<br>
                          .age(30)<br>
                          .build();<br>
장점 : 순서 상관없이 원하는 만큼 초기화 가능 편리하다.<br>
</pre>
#관점 지향 프로그래밍(AOP : Aspect-Oriented Programming)
관점을 기준으로 묶어 개발하는 방식을 의미합니다. 관점(aspect)이란 얻너 기능을 구현할 때 그 기능을 '핵심 기능'과'부가 기능'으로 구분해 각각을 하나의 관점으로 보는 것을 의미<br>
Spring에서는 런타임 시점에서 AOP를 적용합니다<br>

@Data - Lombok에서 제공하는 어노테이션으로, 자바 클래스에 대한 메서드들을 자동으로 생성해주는 역할을 합니다.<br>
      - toString(), equals(), hashCode(), Getter, Setter 메서드가 자동생성됩니다.<br>
 <br>
![image](https://github.com/SeungGwan123/SpringBoot/assets/123438749/85632347-ec2b-41ad-a1b1-7bd9a9c76169)
