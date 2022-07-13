import authenticationStatic.Authentication;
import authenticationStatic.CredentialsStaticService;
import authenticationStatic.PermissionStaticService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

public class AuthenticationStaticMockTest {

    @Test
    public void verifyAuth() {
        MockedStatic<CredentialsStaticService> credencialMocked= Mockito.mockStatic(CredentialsStaticService.class);
        credencialMocked.when(()->CredentialsStaticService.isValidCredential("admin","admin")).thenReturn(true);

        MockedStatic<PermissionStaticService> permissionMocked= Mockito.mockStatic(PermissionStaticService.class);
        permissionMocked.when(()->PermissionStaticService.getPermission("admin")).thenReturn("CRUD");

        Authentication authentication = new Authentication();

        String exceptedResult = "user authenticated successfully with permission: [CRUD]";
        String actualResult = authentication.login("admin", "admin");

        Assertions.assertEquals(exceptedResult, actualResult, "ERROR de autenticación");

        credencialMocked.close();
        permissionMocked.close();
    }


    @ParameterizedTest
    @CsvSource({
            "admin,admin",
            "gaby,gaby",
            "otheruser,otheruser"
    })
    public void verifyAuthWithParams(String user, String pass) {
        MockedStatic<CredentialsStaticService> credencialMocked= Mockito.mockStatic(CredentialsStaticService.class);
        credencialMocked.when(()->CredentialsStaticService.isValidCredential(user,pass)).thenReturn(true);

        MockedStatic<PermissionStaticService> permissionMocked= Mockito.mockStatic(PermissionStaticService.class);
        permissionMocked.when(()->PermissionStaticService.getPermission(user)).thenAnswer(new Answer<String>() {
               public String answer(InvocationOnMock invocation) {
                   if (user.equals("admin"))
                       return "CRUD";
                   else
                       return "READ-ONLY";
               }
           }
        );

        Authentication authentication = new Authentication();

        String exceptedResultAdmin = "user authenticated successfully with permission: [CRUD]";
        String exceptedResult = "user authenticated successfully with permission: [READ-ONLY]";

        String actualResult = authentication.login(user, pass);

        Assertions.assertTrue(List.of(exceptedResultAdmin, exceptedResult).contains(actualResult));

        credencialMocked.close();
        permissionMocked.close();
    }
}
