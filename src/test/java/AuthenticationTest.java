import authentication.Authentication;
import authentication.CredentialsService;
import authentication.PermissionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

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

    @ParameterizedTest
    @CsvSource({
            "admin,admin",
            "lucas,lucas",
            "other,other"
    })
    public void verifyAuthWithParams(String user, String pass) {
        Mockito.when(credentialsMock.isValidCredential(user, pass)).thenReturn(true);

        Mockito.when(permissionMock.getPermission(user)).thenAnswer(new Answer<String>() {
                public String answer(InvocationOnMock invocation) {
                    if (user.equals("admin"))
                        return "CRUD";
                    else
                        return "READ-ONLY";
                }
            }
        );

        Authentication authentication = new Authentication();
        authentication.setCredentials(credentialsMock);
        authentication.setPermission(permissionMock);

        String exceptedResultAdmin = "user authenticated successfully with permission: [CRUD]";
        String exceptedResult = "user authenticated successfully with permission: [READ-ONLY]";

        String actualResult = authentication.login(user, pass);

        Assertions.assertTrue(List.of(exceptedResultAdmin, exceptedResult).contains(actualResult));

        Mockito.verify(credentialsMock).isValidCredential(user, pass);
        Mockito.verify(permissionMock).getPermission(user);
    }
}
