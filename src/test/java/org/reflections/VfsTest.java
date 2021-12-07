package org.reflections;

import javassist.bytecode.ClassFile;
import org.junit.jupiter.api.Test;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.vfs.SystemDir;
import org.reflections.vfs.Vfs;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.text.MessageFormat.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.reflections.ReflectionsQueryTest.equalTo;
import static org.reflections.scanners.Scanners.SubTypes;

public class VfsTest {

<<<<<<< HEAD
=======
public class VfsTest {

>>>>>>> parent of d15d0ec... vfsTest v1
    @Test
    public void testJarFile() throws Exception {
        URL url = new URL(ClasspathHelper.forClass(Logger.class).toExternalForm().replace("jar:", ""));
        assertTrue(url.toString().startsWith("file:"));
        assertTrue(url.toString().contains(".jar"));

        assertTrue(Vfs.DefaultUrlTypes.jarFile.matches(url));
        assertFalse(Vfs.DefaultUrlTypes.jarUrl.matches(url));
        assertFalse(Vfs.DefaultUrlTypes.directory.matches(url));

        Vfs.Dir dir = Vfs.DefaultUrlTypes.jarFile.createDir(url);
        testVfsDir(dir);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    /**
     * Given a jarUrl, vfs should tell it to be url
     * Given a jarUrl, vfs should not say it is file or directory
     * @throws Exception
     */
=======
>>>>>>> parent of d15d0ec... vfsTest v1
=======
>>>>>>> parent of d15d0ec... vfsTest v1
    @Test
    public void testJarUrl() throws Exception {
        URL url = ClasspathHelper.forClass(Logger.class);
        assertTrue(url.toString().startsWith("jar:file:"));
        assertTrue(url.toString().contains(".jar!"));

        assertFalse(Vfs.DefaultUrlTypes.jarFile.matches(url));
        assertTrue(Vfs.DefaultUrlTypes.jarUrl.matches(url));
        assertFalse(Vfs.DefaultUrlTypes.directory.matches(url));

        Vfs.Dir dir = Vfs.DefaultUrlTypes.jarUrl.createDir(url);
        testVfsDir(dir);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    /**
     * Given a directory, vfs should tell it to be directory
     * Given a directory, vfs should not say it is url or file
     * @throws Exception
     */
=======
>>>>>>> parent of d15d0ec... vfsTest v1
=======
>>>>>>> parent of d15d0ec... vfsTest v1
    @Test
    public void testDirectory() throws Exception {
        URL url = ClasspathHelper.forClass(getClass());
        assertTrue(url.toString().startsWith("file:"));
        assertFalse(url.toString().contains(".jar"));

        assertFalse(Vfs.DefaultUrlTypes.jarFile.matches(url));
        assertFalse(Vfs.DefaultUrlTypes.jarUrl.matches(url));
        assertTrue(Vfs.DefaultUrlTypes.directory.matches(url));

        Vfs.Dir dir = Vfs.DefaultUrlTypes.directory.createDir(url);
        testVfsDir(dir);
    }

<<<<<<< HEAD
<<<<<<< HEAD
    /**
     * vfs can tell in the givne url can be interpreted as input stream
     * @throws Exception
     */
=======
>>>>>>> parent of d15d0ec... vfsTest v1
=======
>>>>>>> parent of d15d0ec... vfsTest v1
    @Test
    public void testJarInputStream() throws Exception {
        URL url = ClasspathHelper.forClass(Logger.class);
        assertTrue(Vfs.DefaultUrlTypes.jarInputStream.matches(url));
        try {
            testVfsDir(Vfs.DefaultUrlTypes.jarInputStream.createDir(url));
            fail();
        } catch (ReflectionsException e) {
            // expected
        }

        url = new URL(ClasspathHelper.forClass(Logger.class).toExternalForm().replace("jar:", "").replace(".jar!", ".jar"));
        assertTrue(Vfs.DefaultUrlTypes.jarInputStream.matches(url));
        testVfsDir(Vfs.DefaultUrlTypes.jarInputStream.createDir(url));

        url = ClasspathHelper.forClass(getClass());
        assertFalse(Vfs.DefaultUrlTypes.jarInputStream.matches(url));
        try {
            testVfsDir(Vfs.DefaultUrlTypes.jarInputStream.createDir(url));
            fail();
        } catch (AssertionError e) {
            // expected
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    /**
     * edge case: directory with space in name
     */
=======
>>>>>>> parent of d15d0ec... vfsTest v1
=======
>>>>>>> parent of d15d0ec... vfsTest v1
    @Test
    public void dirWithSpaces() {
        Collection<URL> urls = ClasspathHelper.forPackage("dir+with spaces");
        assertFalse(urls.isEmpty());
        for (URL url : urls) {
            Vfs.Dir dir = Vfs.fromURL(url);
            assertNotNull(dir);
            assertNotNull(dir.getFiles().iterator().next());
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    /**
     * using directory name as input
     * @throws MalformedURLException
     */
=======
>>>>>>> parent of d15d0ec... vfsTest v1
=======
>>>>>>> parent of d15d0ec... vfsTest v1
    @Test
    public void vfsFromDirWithJarInName() throws MalformedURLException {
        String tmpFolder = System.getProperty("java.io.tmpdir");
        tmpFolder = tmpFolder.endsWith(File.separator) ? tmpFolder : tmpFolder + File.separator;
        String dirWithJarInName = tmpFolder + "tony.jarvis";
        File newDir = new File(dirWithJarInName);
        newDir.mkdir();

        try {
            Vfs.Dir dir = Vfs.fromURL(new URL(format("file:{0}", dirWithJarInName)));

            assertEquals(dirWithJarInName.replace("\\", "/"), dir.getPath());
            assertEquals(SystemDir.class, dir.getClass());
        } finally {
            newDir.delete();
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    /**
     * using directory as input
     * @throws MalformedURLException
     */
=======
>>>>>>> parent of d15d0ec... vfsTest v1
=======
>>>>>>> parent of d15d0ec... vfsTest v1
    @Test
    public void vfsFromDirWithJarInJar() throws Exception {
        URL resource = ClasspathHelper.contextClassLoader().getResource("jarWithBootLibJar.jar");
        URL innerJarUrl = new URL("jar:" + resource.toExternalForm() + "!/BOOT-INF/lib/jarWithManifest.jar");

        assertFalse(Vfs.DefaultUrlTypes.jarUrl.matches(innerJarUrl));
        Vfs.Dir jarUrlDir = Vfs.DefaultUrlTypes.jarUrl.createDir(innerJarUrl);
        assertNotEquals(innerJarUrl.getPath(), jarUrlDir.getPath());

        assertTrue(Vfs.DefaultUrlTypes.jarInputStream.matches(innerJarUrl));
        Vfs.Dir jarInputStreamDir = Vfs.DefaultUrlTypes.jarInputStream.createDir(innerJarUrl);
        assertEquals(innerJarUrl.getPath(), jarInputStreamDir.getPath());

        List<Vfs.File> files = StreamSupport.stream(jarInputStreamDir.getFiles().spliterator(), false).collect(Collectors.toList());
        assertEquals(1, files.size());
        Vfs.File file1 = files.get(0);
        assertEquals("empty.class", file1.getName());
        assertEquals("pack/empty.class", file1.getRelativePath());

        for (Vfs.File file : jarInputStreamDir.getFiles()) {
            try (DataInputStream dis = new DataInputStream(new BufferedInputStream(file.openInputStream()))) {
                ClassFile classFile = new ClassFile(dis);
                assertEquals("org.reflections.empty", classFile.getName());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
    /**
     * url directory should be distinguished by vfs
     * @throws Exception
     */
    void testUrlDir() throws Exception{
        URL url = new URL(ClasspathHelper.forClass(Logger.class).toExternalForm().replace("jar:", ""));

        assertTrue(url.toString().startsWith("file:"));
        assertTrue(url.toString().contains(".jar"));

        assertFalse(urls.isEmpty());
        for (URL url : urls) {
            Vfs.Dir dir = Vfs.fromURL(url);
            assertNotNull(dir);
            assertNotNull(dir.getFiles().iterator().next());
        }
    }

    /**
     * ExpandSupertypes
     *
     * should be distinguished by vfs
     */
    @Test
    public void testExpandSupertypes() {
        ConfigurationBuilder configuration = new ConfigurationBuilder()
                .forPackage("org.reflections")
                .filterInputsBy(inputsFilter);

        Reflections reflections = new Reflections(configuration);
        assertThat(reflections.get(SubTypes.of(ReflectionsExpandSupertypesTest.ExpandTestModel.NotScanned.BaseInterface.class).asClass()),
                equalTo(
                        ReflectionsExpandSupertypesTest.ExpandTestModel.NotScanned.BaseClass.class,
                        ReflectionsExpandSupertypesTest.ExpandTestModel.Scanned.ChildrenClass.class));

        Reflections refNoExpand = new Reflections(configuration.setExpandSuperTypes(false));
        assertThat(refNoExpand.get(SubTypes.of(ReflectionsExpandSupertypesTest.ExpandTestModel.NotScanned.BaseInterface.class).asClass()),
                equalTo());
    }

    /**
     * Call by testJarFile, construct directory from a file
     * @param dir
     */
=======
>>>>>>> parent of d15d0ec... vfsTest v1
=======
>>>>>>> parent of d15d0ec... vfsTest v1
    private void testVfsDir(Vfs.Dir dir) {
        List<Vfs.File> files = new ArrayList<>();
        for (Vfs.File file : dir.getFiles()) {
            files.add(file);
        }
        assertFalse(files.isEmpty());
    }
}
