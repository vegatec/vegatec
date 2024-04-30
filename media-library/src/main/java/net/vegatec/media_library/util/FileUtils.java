package net.vegatec.media_library.util;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class FileUtils {

	private static Logger logger = Logger.getLogger("FileUtils");

	public static List search(final File folder, final FilenameFilter filter) {
		final List<File> found = new ArrayList<File>();
		FolderVisitor visitor= new  FolderVisitor() {
			protected  void process(File file) {
				if (filter.accept(file.getParentFile(), file.getName())) {
//					if (logger.isDebugEnabled()) {
//						logger.debug(file);
//
//					}
					found.add(file);
				}
			}

		};
		visitor.visitAllFiles(folder);
		return found;
	}


//	 Copies all files under srcDir to dstDir.
    // If dstDir does not exist, it will be created.
    public void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }

            String[] children = srcDir.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(srcDir, children[i]),
                                     new File(dstDir, children[i]));
            }
        } else {
            // This method is implemented in e1071 Copying a File
            copyFile(srcDir, dstDir);
        }
    }

//  Copies src file to dst file.
    // If the dst file does not exist, it is created
    void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }



	public static String getRelativePath(File absolutePath, File rootFolder) {
		return getRelativePath(absolutePath.getAbsolutePath(), rootFolder
				.getAbsolutePath());
	}

	public static String getRelativePath(String absolutePath, String rootFolder) {
		if (absolutePath == null || rootFolder == null)
			throw new IllegalArgumentException("null arguments");

		if (rootFolder.endsWith("/"))
			rootFolder.substring(rootFolder.length() - 1);

		if (absolutePath.startsWith(rootFolder)) {
			return absolutePath.substring(rootFolder.length() + 1);
		}

		return absolutePath;
	}



//	public static void main(String[] args) {
//		long start= System.currentTimeMillis();
//		List filesFound = FileUtils
//				.search(
//						new File(
//								"C:/Music"),
//						new FilenameFilter() {
//
//							public boolean accept(File dir, String name) {
//								if (logger.isDebugEnabled()) {
//									logger.debug("dir: " + dir);
//									logger.debug("name: " + name);
//								}
//
//								return (name.toLowerCase().endsWith(".mp3"));
//							}
//
//						});
//
//		System.out.println((System.currentTimeMillis()-start)/1000);
//		System.out.println(filesFound.size());
//	}

}
