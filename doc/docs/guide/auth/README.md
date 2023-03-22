# 权限认证
权限包含菜单权限、按钮权限、方法调用权限几种类型。我们认为每个账号都拥有一个权限码集合如["1-add","1-del","1-update"]。

对一个账号的操作校验其是否具有权限，也就是判断当前操作的权限码是否包含在账号的权限码集合中。

此时需要回答的两个问题是：

1. 账号的权限码集合如何获取？

答：scrwe不会提供一个设计好的权限模型，而是将获取权限码的逻辑定义为一个接口，由业务系统根据自己的业务场景进行实现

3. 如何知道每次请求操作的权限码是什么？

答：不同框架有不同写法，一般思路下，在登录时，后端需要将菜单、按钮对应的权限码集合发送到前端，前端在每次点击菜单或按钮时，将权限码放入参数中进行传递。

## 定义账号权限码集合
由于每个业务系统的权限设计千变万化，scrwe不会提供一个设计好的权限模型，而是将获取权限码的逻辑定义为一个接口，由业务系统根据自己的业务场景进行实现

你只需要实现`AuthCheckProvider`接口即可，代码示例：
```java
import com.ds.screw.auth.provider.AuthCheckProvider;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthCheckProvider implements AuthCheckProvider {

    /**
     * 返回账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId) {
        List<String> list = new ArrayList<>();
        list.add("meun.add");
        list.add("meun.update");
        list.add("meun.get");
        // 支持通配符校验
        list.add("meun.*");
        return list;
    }

    /**
     * 返回账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId) {
        List<String> list = new ArrayList<>();
        list.add("admin");
        list.add("test1");
        list.add("test2");
        return list;
    }
}
```

## 权限校验
在定义好权限码的获取方式后，就可以使用一下API来进行鉴权了
```java
// 获取：当前账号所拥有的权限集合
AuthUtils.getPermissionList();

// 判断：当前账号是否含有指定权限, 返回 true 或 false
AuthUtils.hasPermission("menu.add");        

// 校验：当前账号是否含有指定权限, 如果验证未通过，则抛出异常: NotPermissionException 
AuthUtils.checkPermission("menu.add");        

// 校验：当前账号是否含有指定权限 [指定多个，必须全部验证通过]
AuthUtils.checkPermissionAnd("menu.add", "menu.delete", "menu.get");        

// 校验：当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
AuthUtils.checkPermissionOr("menu.add", "menu.delete", "menu.get");  
```
::: tip 提示
校验不通过时将抛出`NotHasPermisssionException`异常，可通过其 getPermission() 方法获取不通过的权限码
:::


## 角色校验
在screw中，角色可以独立进行校验

```java
// 获取：当前账号所拥有的角色集合
AuthUtils.getRoleList();

// 判断：当前账号是否拥有指定角色, 返回 true 或 false
AuthUtils.hasRole("admin");

// 校验：当前账号是否含有指定角色标识, 如果验证未通过，则抛出异常: NotRoleException
AuthUtils.checkRole("admin");

// 校验：当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
AuthUtils.checkRoleAnd("admin", "test-user");

// 校验：当前账号是否含有指定角色标识 [指定多个，只要其一验证通过即可] 
AuthUtils.checkRoleOr("admin", "test-user");

```
::: tip 提示
校验不通过时将抛出`NotHasRoleException`异常，可通过其 getRole() 方法获取不通过的角色名
:::


## 通配符校验
screw允许你根据通配符指定泛权限，例如当一个账号拥有user.*的权限时，user.add、user.delete、user.update都将匹配通过

```java
// 当拥有 user.* 权限时
AuthUtils.hasPermission("user.add");        // true
AuthUtils.hasPermission("user.update");     // true
AuthUtils.hasPermission("goods.add");      // false

// 当拥有 *.delete 权限时
AuthUtils.hasPermission("user.delete");      // true
AuthUtils.hasPermission("user.delete");     // true
AuthUtils.hasPermission("user.update");     // false

// 当拥有 *.js 权限时
AuthUtils.hasPermission("index.js");        // true
AuthUtils.hasPermission("index.css");       // false
AuthUtils.hasPermission("index.html");      // false


```

## 注解鉴权
尽管使用代码鉴权非常方便，但是有时仍希望把鉴权逻辑和业务逻辑分离开来，此时可以使用注解鉴权的方式

注解鉴权 —— 优雅的将鉴权与业务代码分离！

* @SaCheckLogin: 登录校验 —— 只有登录之后才能进入该方法。
* @SaCheckRole("admin"): 角色校验 —— 必须具有指定角色标识才能进入该方法。
* @SaCheckPermission("user:add"): 权限校验 —— 必须具有指定权限才能进入该方法。

screw 通过AOP的方式来完成注解鉴权，使用时请确保开启了`@EnableAspectJAutoProxy` 注解。

### 如何使用
```java
// 登录校验：只有登录之后才能进入该方法 
@SaCheckLogin                        
@RequestMapping("info")
public String info() {
    return "查询用户信息";
}

// 角色校验：必须具有指定角色才能进入该方法 
@SaCheckRole("admin")        
@RequestMapping("add")
public String add() {
    return "用户增加";
}

// 权限校验：必须具有指定权限才能进入该方法 
@SaCheckPermission("user-add")        
@RequestMapping("add")
public String add() {
    return "用户增加";
}
```

### 角色权限双重或"or"校验
当一个接口需要在具有权限 user.add 或角色 admin 时可以调通时，可以这样配置
```java
// 角色权限双重 “or校验”：具备指定权限或者指定角色即可通过校验
@RequestMapping("userAdd")
@SaCheckPermission(value = "user.add", orRole = "admin")        
public ApiResponse userAdd() {
    return ApiResponse.data("用户信息");
}
```
orRole 字段代表权限认证未通过时的次要选择，两者只要其一认证成功即可通过校验，其有三种写法：
* 写法一：orRole = "admin"，代表需要拥有角色 admin 。
* 写法二：orRole = {"admin", "manager", "test"}，代表具有三个角色其一即可。
* 写法三：orRole = {"admin, manager, test"}，代表必须同时具有三个角色。