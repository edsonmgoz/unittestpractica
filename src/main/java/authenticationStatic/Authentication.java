package authenticationStatic;

public class Authentication {

    public Authentication(){
    }

    public String login(String user, String pwd){
        if (CredentialsStaticService.isValidCredential(user,pwd)){
            String permission=PermissionStaticService.getPermission(user);
            return "user authenticated successfully with permission: ["+permission+"]";
        }else{
            return "user or password incorrect";
        }
    }
}
