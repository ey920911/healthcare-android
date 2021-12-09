package example.com.wy_WindowPartition;

import java.io.File;
import java.util.ArrayList;
import knn.finalStaticVariables;

/**
 * file directory 를 설정하면 그 directory 안에 있는 file 목록을 수집하여 리스트로 리턴 하는 클래스이다.
 */

public class MyFileReader {
  String mFolderPath;
  ArrayList<String> mPaths;
  File mFolder;

  public MyFileReader() {
    mPaths = new ArrayList<String>();
    mFolder = null;
    mFolder = null;
  }

  public void setFolder(String folderPath) {
    this.mFolderPath = folderPath;
    mFolder = new File(mFolderPath);
    if (!mFolder.exists()) mFolder.mkdirs();
    mPaths = new ArrayList<String>();
    finalStaticVariables.debugPrintln("setFolder(" + folderPath + ") is set");
  }

  public ArrayList<String> getCSVList() {
    return mPaths;
  }

  public void printCSVList() {
    System.out.println("\nPrint CVS List Start");
    for (String path : mPaths) {
      System.out.println(path);
    }
    System.out.println("Print CVS List End.\n");
  }

  public void setFilePathList() {
    setFilePathList(mFolder);
  }

  public void setFilePathList(String dirString) {
    setFilePathList(new File(dirString));
  }

  public void setFilePathList(File dirFile) {
    setFilePathList(dirFile, null);
  }

  public void setFilePathList(File dirFile, String fileExtension) {
    File[] fileList = dirFile.listFiles();
    finalStaticVariables.debugPrintln("File Reading start...");
    if (fileExtension != null) {
      finalStaticVariables.debugPrintln(
        " with only \"." + fileExtension + "\" files"
      );
    }
    for (File tempFile : fileList) {
      if (tempFile.isFile()) {
        if (fileExtension != null) {
          String filename = tempFile.getName();
          String extension = filename.substring(
            filename.lastIndexOf(".") + 1,
            filename.length()
          );
          if (!extension.equals(fileExtension)) continue;
        }
        String tempPath = tempFile.getParent();
        String tempFileName = tempFile.getName();
        finalStaticVariables.debugPrintln(
          "Path=" + tempPath + "/" + tempFileName
        );
        mPaths.add(tempPath + "/" + tempFileName);
      }
    }
    finalStaticVariables.debugPrintln("File Reading end...");
  }
}
