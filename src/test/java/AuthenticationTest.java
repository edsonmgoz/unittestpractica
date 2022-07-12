import authentication.Authentication;
import authentication.CredentialsService;
import authentication.PermissionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AuthenticationTest {

    //Authentication auth = Mockito.mock(Authentication.class);
    PermissionService permissionMock = Mockito.mock(PermissionService.class);
    CredentialsService credentialsMock = Mockito.mock(CredentialsService.class);

    @Test
    public void verifyAuth() {
        Mockito.when(credentialsMock.isValidCredential("admin", "admin")).thenReturn(true);
        Mockito.when(permissionMock.getPermission("admin")).thenReturn("CRUD");

        Authentication authentication = new Authentication();
        authentication.setCredentials(credentialsMock);
        authentication.setPermission(permissionMock);

        String exceptedResult = "user authenticated successfully with permission: [CRUD]";
        String actualResult = authentication.login("admin", "admin");

        Assertions.assertEquals(exceptedResult, actualResult, "ERROR de autenticación");

        Mockito.verify(credentialsMock).isValidCredential("admin", "admin");
        Mockito.verify(permissionMock).getPermission("admin");
    }
}
