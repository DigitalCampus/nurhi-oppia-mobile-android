import android.provider.MediaStore;

import org.digitalcampus.oppia.utils.storage.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


public class FileUtilsTests {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private static final int FILES_COUNT = 5;

    @Test
    public void UnzipFiles_correctPaths(){
        File zipFile;
        boolean result = false;
        try {
            zipFile = createTestZipFile();

            if (zipFile != null) {
                result = FileUtils.unzipFiles(zipFile.getParentFile().getAbsolutePath(),
                        zipFile.getName(),
                        zipFile.getParentFile().getAbsolutePath());
            }

            assertTrue(result);
            assertEquals(FILES_COUNT + 1, zipFile.getParentFile().listFiles().length);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void UnzipFiles_wrongPaths(){
        File zipFile;
        boolean result;
        try {
            zipFile = createTestZipFile();


            if (zipFile != null) {
                //Source directory equals null
                result = FileUtils.unzipFiles("Non_Existing_path",
                        zipFile.getName(),
                        zipFile.getParentFile().getAbsolutePath());


                assertFalse(result);

                //Source file equals null
                result = FileUtils.unzipFiles(zipFile.getParentFile().getAbsolutePath(),
                        "Non_Existing_path",
                        zipFile.getParentFile().getAbsolutePath());


                assertFalse(result);

                //Destination directory equals null
                result = FileUtils.unzipFiles(zipFile.getParentFile().getAbsolutePath(),
                        zipFile.getName(),
                        "Non_Existing_path");


                assertFalse(result);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void CleanDir_correctPath(){

        try {
            File tempFolder = folder.newFolder("tempFolder");

            for (int i = 0; i < FILES_COUNT; i++) {
                String filename = "test_file" + i + ".txt";
                new File(tempFolder + File.separator + filename).createNewFile();
            }

            FileUtils.cleanDir(tempFolder);

            assertEquals(0, tempFolder.listFiles().length);

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void CleanDir_wrongPath(){
        File tempFile = new File("tempFile");
        assertTrue(FileUtils.cleanDir(tempFile));
    }

    @Test
    public void CleanDir_fileAsArgument(){

        try {
            File tempFile = folder.newFile("tempFile.txt");

            assertTrue(FileUtils.cleanDir(tempFile));

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void DeleteDir(){
        try{
            File tempFolder = folder.newFolder("tempFolder");

            assertTrue(FileUtils.deleteDir(tempFolder));
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void DirSize(){
        //Case when the directory does not exists
        File f = new File("non_exists_dir");
        assertEquals(0, FileUtils.dirSize(f));

        try {
            //Case when the file is not a directory
            f = folder.newFile("not_directory.txt");
            assertEquals(0, FileUtils.dirSize(f));

            //Case when the file exists and is a directory
            File tempFolder = folder.newFolder("tempFolder");
            String text = "The quick brown fox jumps over the lazy dog";

            for (int i = 0; i < FILES_COUNT; i++) {
                String filename = "test_file" + i + ".txt";
                File file = new File(tempFolder + File.separator + filename);

                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(text);
                fileWriter.close();
            }

            assertEquals(text.length() * FILES_COUNT, FileUtils.dirSize(tempFolder));
        }catch(IOException ioe){
            ioe.printStackTrace();
        }

    }

    @Test
    public void CleanUp(){
        try {
            File dir = folder.newFolder("testFolder");
            File zipFile = folder.newFile("zipFile.zip");

            FileUtils.cleanUp(dir, zipFile.getAbsolutePath());

            assertFalse(dir.exists());
            assertFalse(zipFile.exists());

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @Test
    public void ReadFile(){
        String text = "The quick brown fox jumps over the lazy dog";
        String filename = "test_file.txt";

        try {
            File file = folder.newFile(filename);
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.close();

            FileInputStream fis = new FileInputStream(file);
            assertEquals(text, FileUtils.readFile(fis));
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

   /* @Test
    public void FileUtils_supportedMediafileType(){
        assertFalse(FileUtils.supportedMediafileType(null));

        assertTrue(FileUtils.supportedMediafileType("video/m4v"));

        assertTrue(FileUtils.supportedMediafileType("video/mp4"));

        assertTrue(FileUtils.supportedMediafileType("audio/mpeg"));

        assertFalse(FileUtils.supportedMediafileType("application/json"));

    }
*/

    private File createTestZipFile() throws IOException {

        //Create the files that will be zipped

        File zipFile = null;
        File[] files = new File[FILES_COUNT];
        try {
            for (int i = 0; i < FILES_COUNT; i++) {
                String filename = "test_file" + i + ".txt";
                files[i] = folder.newFile(filename);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedInputStream is;
        ZipOutputStream out = null;
        try {
            zipFile = folder.newFile("test.zip");
            out = new ZipOutputStream((new BufferedOutputStream(new FileOutputStream(zipFile))));

            byte data[] = new byte[FileUtils.BUFFER_SIZE];

            for(int i = 0; i < files.length; i++){
                FileInputStream fis = new FileInputStream(files[i]);
                is = new BufferedInputStream(fis, FileUtils.BUFFER_SIZE);
                try{
                    ZipEntry entry = new ZipEntry(files[i].getName());
                    out.putNextEntry(entry);

                    int count;
                    while ((count = is.read(data, 0, FileUtils.BUFFER_SIZE)) != -1){
                        out.write(data, 0, count);
                    }
                }catch(IOException ioe){
                    ioe.printStackTrace();
                }finally{
                    is.close();
                    files[i].delete();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally{
            out.close();
        }

        return zipFile;

    }
}
