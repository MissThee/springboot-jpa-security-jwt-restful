# SpringBootSecurityDemo
spring boot REST security URL and METHOD annotation

## 拦截机制
先通过http request 级别的过滤<br>
即所有的访问都必须满足拥有 ``USER``权限
```java
  @Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.csrf().disable();
		http
			.authorizeRequests()
						.antMatchers("/**").hasRole("USER")
						.and()
						.httpBasic()
						;
		// @formatter:on				
	}
```
再通过方法级别的验证<br>
也就是说想要访问``/adminAT``的用户所拥有的``roles``必须包含``USER``<br>
像这样``.withUser("provider").password("providerTenmax").roles("PROVIDER", "USER")``<br>
这样才能成功使用``provider``用户访问``/adminAT``接口
```java
  @RolesAllowed({"ADMIN","PROVIDER"})
  	@RequestMapping(value = "/adminAT", method = RequestMethod.GET)
  	public @ResponseBody String adminAT() {
  		System.out.println("user adminAT login !");
  		return "user adminAT login";
  	}
```
