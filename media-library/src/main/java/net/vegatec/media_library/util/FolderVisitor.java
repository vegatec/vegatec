package net.vegatec.media_library.util;

import java.io.File;

public abstract class FolderVisitor {

	public  void visitAllFoldersAndFiles(File folder) {
		process(folder);
		String[] children = folder.list();
		for (int i = 0; i < children.length; i++) {
			visitAllFoldersAndFiles(new File(folder, children[i]));
		}

	}

	public  void visitAllFolders(File folder) {
		if (folder.isDirectory()) {
			process(folder);
			String[] children = folder.list();
			for (int i = 0; i < children.length; i++) {
				visitAllFolders(new File(folder, children[i]));
			}

		}

	}

	public  void visitAllFiles(File folder) {
		if (folder.isDirectory()) {
			String[] children = folder.list();
			for (int i = 0; i < children.length; i++) {
				visitAllFiles(new File(folder, children[i]));
			}

		} else {
			process(folder);
		}

	}

	protected abstract void process(File folder);
}
