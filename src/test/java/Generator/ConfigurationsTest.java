package Generator;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConfigurationsTest {
    @Test
    public void getConfigurations() {
        assertNotEquals(null, Configurations.getConfigurations());
        assertEquals(Configurations.getConfigurations(), Configurations.getConfigurations());
    }

    @Test
    public void filePath() {
        String filePath = "hello";
        assertEquals(null, Configurations.getFilePath());
        Configurations.setFilePath(filePath);
        assertEquals(filePath, Configurations.getFilePath());
        Configurations.setFilePath(null);
        assertEquals(null, Configurations.getFilePath());
    }

    @Test
    public void defaultInterfacesConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(2, configurations.interfaces.min);
        assertEquals(5, configurations.interfaces.max);
        assertTrue(configurations.interfaces.current >= configurations.interfaces.min);
        assertTrue(configurations.interfaces.current <= configurations.interfaces.max);
    }

    @Test
    public void defaultAbstractClassesConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(3, configurations.abstractClasses.min);
        assertEquals(7, configurations.abstractClasses.max);
        assertTrue(configurations.abstractClasses.current >= configurations.abstractClasses.min);
        assertTrue(configurations.abstractClasses.current <= configurations.abstractClasses.max);
    }

    @Test
    public void defaultClassesConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(5, configurations.classes.min);
        assertEquals(10, configurations.classes.max);
        assertTrue(configurations.classes.current >= configurations.classes.min);
        assertTrue(configurations.classes.current <= configurations.classes.max);
    }

    @Test
    public void defaultInheritanceHierarchyConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(1, configurations.inheritanceHierarchy.min);
        assertEquals(4, configurations.inheritanceHierarchy.max);
        assertEquals(-1, configurations.inheritanceHierarchy.current);
    }

    @Test
    public void defaultInterfaceHierarchyConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(1, configurations.interfaceHierarchy.min);
        assertEquals(4, configurations.interfaceHierarchy.max);
        assertEquals(-1, configurations.interfaceHierarchy.current);
    }

    @Test
    public void defaultInterfaceMethodsConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(1, configurations.interfaceMethods.min);
        assertEquals(3, configurations.interfaceMethods.max);
        assertEquals(-1, configurations.interfaceMethods.current);
    }

    @Test
    public void defaultInterfaceConstantsConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(0, configurations.interfaceConstants.min);
        assertEquals(2, configurations.interfaceConstants.max);
        assertEquals(-1, configurations.interfaceConstants.current);
    }

    @Test
    public void defaultOutputDirectoryConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        assertEquals("GeneratedClasses", Configurations.getConfigurations().outputDirectory);
    }

    @Test
    public void defaultMethodParametersConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(0, configurations.methodParameters.min);
        assertEquals(3, configurations.methodParameters.max);
        assertEquals(-1, configurations.methodParameters.current);
    }

    @Test
    public void defaultAbstractMethodsConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(1, configurations.abstractMethods.min);
        assertEquals(3, configurations.abstractMethods.max);
        assertEquals(-1, configurations.abstractMethods.current);
    }

    @Test
    public void defaultConcreteMethodsConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(1, configurations.concreteMethods.min);
        assertEquals(5, configurations.concreteMethods.max);
        assertEquals(-1, configurations.concreteMethods.current);
    }

    @Test
    public void defaultBehaviorsConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(0, configurations.behaviors.min);
        assertEquals(2, configurations.behaviors.max);
        assertEquals(-1, configurations.behaviors.current);
    }

    @Test
    public void defaultGenerationTypeConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(Configurations.GenerationType.SOURCECODE, configurations.generationType);
    }

    @Test
    public void defaultFieldsConfiguration() {
        assertEquals(null, Configurations.getFilePath());
        Configurations configurations = Configurations.getConfigurations();
        assertEquals(1, configurations.fields.min);
        assertEquals(5, configurations.fields.max);
        assertEquals(-1, configurations.fields.current);
    }
}