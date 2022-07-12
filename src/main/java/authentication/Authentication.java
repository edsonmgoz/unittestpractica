package authentication;

public class Authentication {
    private PermissionService permissionService;
    private CredentialsService credentialsService;

    public Authentication(){
        permissionService=new PermissionService();
        credentialsService= new CredentialsService();
    }

    public void setPermission(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    public void setCredentials(CredentialsService credentialsService) {
        this.credentialsService = credentialsService;
    }

    public String login(String user, String pwd){
        if (credentialsService.isValidCredential(user,pwd)){
            String permission=permissionService.getPermission(user);
            return "user authenticated successfully with permission: ["+permission+"]";
        }else{
            return "user or password incorrect";
        }
    }
}
