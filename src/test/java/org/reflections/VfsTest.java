package org.reflections;

import javassist.bytecode.ClassFile;
import org.junit.jupiter.api.Test;
import org.reflections.util.ClasspathHelper;
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
import static org.junit.jupiter.api.Assertions.*;

class VfsTest {
    /**
     * Given a jarFile, vfs should tell it to be file
     * Given a jarFile, vfs should not say it is url or directory
     * @throws Exception
     */
    @Test
    void testJarFile() throws Exception {
        URL url = new URL(ClasspathHelper.forClass(Logger.class).toExternalForm().replace("jar:", ""));

        assertTrue(url.toString().startsWith("file:"));
        assertTrue(url.toString().contains(".jar"));

        assertTrue(Vfs.DefaultUrlTypes.jarFile.matches(url));
        assertFalse(Vfs.DefaultUrlTypes.jarUrl.matches(url));
        assertFalse(Vfs.DefaultUrlTypes.directory.matches(url));

        Vfs.Dir dir = Vfs.DefaultUrlTypes.jarFile.createDir(url);
        testVfsDir(dir);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    void testJarUrl() throws Exception {
        URL url = ClasspathHelper.forClass(Logger.class);
        assertTrue(url.toString().startsWith("jar:file:"));
        assertTrue(url.toString().contains(".jar!"));

        assertFalse(Vfs.DefaultUrlTypes.jarFile.matches(url));
        assertTrue(Vfs.DefaultUrlTypes.jarUrl.matches(url));
        assertFalse(Vfs.DefaultUrlTypes.directory.matches(url));

        Vfs.Dir dir = Vfs.DefaultUrlTypes.jarUrl.createDir(url);
        testVfsDir(dir);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    void testDirectory() throws Exception {
        URL url = ClasspathHelper.forClass(getClass());
        assertTrue(url.toString().startsWith("file:"));
        assertFalse(url.toString().contains(".jar"));

        assertFalse(Vfs.DefaultUrlTypes.jarFile.matches(url));
        assertFalse(Vfs.DefaultUrlTypes.jarUrl.matches(url));
        assertTrue(Vfs.DefaultUrlTypes.directory.matches(url));

        Vfs.Dir dir = Vfs.DefaultUrlTypes.directory.createDir(url);
        testVfsDir(dir);
    }

    /**
     *
     * @throws Exception
     */
    @Test
    void testJarInputStream() throws Exception {
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

    /**
     *
     */
    @Test
    void dirWithSpaces() {
        Collection<URL> urls = ClasspathHelper.forPackage("dir+with spaces");
        assertFalse(urls.isEmpty());
        for (URL url : urls) {
            Vfs.Dir dir = Vfs.fromURL(url);
            assertNotNull(dir);
            assertNotNull(dir.getFiles().iterator().next());
        }
    }

    /**
     *
     * @throws MalformedURLException
     */
    @Test
    void vfsFromDirWithJarInName() throws MalformedURLException {
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

    /**
     *
     * @throws MalformedURLException
     */
    @Test
    void vfsFromDirWithJarInJar() throws MalformedURLException {
        URL resource = ClasspathHelper.contextClassLoader().getResource("jarWithBootLibJar.jar");
        URL innerJarUrl = new URL("jar:" + resource.toExternalForm() + "!/BOOT-INF/lib/jarWithManifest.jar");


        Vfs.Dir jarUrlDir = null;
        try {
            assertFalse(Vfs.DefaultUrlTypes.jarUrl.matches(innerJarUrl));
            jarUrlDir = Vfs.DefaultUrlTypes.jarUrl.createDir(innerJarUrl);
            assertNotEquals(innerJarUrl.getPath(), jarUrlDir.getPath());
        } catch (Exception e) {
            //e.printStackTrace();
        }

        Vfs.Dir jarInputStreamDir = null;
        try {
            assertTrue(Vfs.DefaultUrlTypes.jarInputStream.matches(innerJarUrl));
            jarInputStreamDir = Vfs.DefaultUrlTypes.jarInputStream.createDir(innerJarUrl);
            assertEquals(innerJarUrl.getPath(), jarInputStreamDir.getPath());
        } catch (Exception e) {
            //e.printStackTrace();
        }

        assertNotEquals(jarInputStreamDir,null);
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
                //e.printStackTrace();
            }
        }
    }

    /**
     * Call by testJarFile, construct directory from a file
     * @param dir
     */
    private void testVfsDir(Vfs.Dir dir) {
        List<Vfs.File> files = new ArrayList<>();
        for (Vfs.File file : dir.getFiles()) {
            files.add(file);
        }
        assertFalse(files.isEmpty());
    }
}
