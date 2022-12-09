package snippets;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

	/***
	 * 
	 * @param file {@linkplain File} object of compressed file
	 * @return path of the uncompressed file
	 */

	public static String unZip(File file) {
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(file);
		} catch (IOException e) {
			System.out.println("Unable to create ZIP instance => " + e);
			return null;
		}

		String unzipBasePath = file.getParent() + File.separator;
		String unzipFilePath = unzipBasePath + file.getName().substring(0, file.getName().lastIndexOf('.')) + "-unzip";

		File unzipFile = new File(unzipFilePath);
		unzipFile.mkdir();

		zipFile.stream().forEach(entry -> {
			try {
				File tpFile = new File(unzipFilePath + File.separator + entry.toString());
				if (writeEntryToFile(zipFile, entry, tpFile))
					System.out.println("SUCCESS -> " + tpFile.getName());
				else
					System.out.println("Failed => " + tpFile.getName());
			} catch (IOException e) {
				System.out.println("Exception while, " + e);
			}
		});

		try {
			if (zipFile != null)
				zipFile.close();
		} catch (IOException e) {
			System.out.println("Exception while, " + e);
		}

		System.out.println("Uncompressed into " + unzipFile.getAbsolutePath());

		return unzipFilePath;
	}

	public static boolean writeEntryToFile(ZipFile zip, ZipEntry entry, File file) throws IOException {
		ZipOutputStream zos = null;
		ZipInputStream zis = null;

		if (!file.exists()) {
			new File(file.getParent()).mkdir();
			file.createNewFile();
		}

		try {
			zos = new ZipOutputStream(new FileOutputStream(file));
			zis = new ZipInputStream(zip.getInputStream(entry));

			byte[] bytes = zis.readAllBytes();
			zos.write(bytes);
			zos.flush();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (zos != null)
				zos.close();
			if (zis != null)
				zis.close();
		}
		return true;
	}

	public static double files = 0, left = 0, right = 0;

	public static boolean zip(File unZipFile) throws IOException {
		if (unZipFile.isFile()) {
			System.out.println("unZipFile is a File, not a Compressed Directory.");
			return false;
		}

		File zipFile = new File(
				unZipFile.getParentFile().getCanonicalPath() + File.separator + unZipFile.getName() + ".zip");
		zipFile.createNewFile();

		String entryReplace = unZipFile.getCanonicalPath() + File.separator;
		FileOutputStream fout = new FileOutputStream(zipFile);
		ZipOutputStream zout = new ZipOutputStream(fout);

		List<String> entries = getEntries(unZipFile);

		files = entries.size();
		left = right = 0;

		for (int i = 0; i <= 100; i++)
			System.out.print('-');
		System.out.println();

		entries.stream().forEachOrdered(entry -> writeAndZipIt(entryReplace, zout, entry));

		zout.close();
		System.out.println("\nCompleted uncompressed");
		return true;
	}

	private static synchronized void writeAndZipIt(String entryReplace, ZipOutputStream zout, String entry) {
		right++;

		while (left < right) {
			System.out.print('.');
			left += (files / 100d);
		}

		try {
			ZipEntry ze = new ZipEntry(entry.replace(entryReplace, ""));
			synchronized (zout) {
				zout.putNextEntry(ze);
				zout.write(Files.readAllBytes(Path.of(entry)));
				zout.flush();
				zout.closeEntry();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static List<String> getEntries(File file) {
		if (file.isFile())
			return Arrays.asList(file.getAbsolutePath());
		List<String> entries = List.of(file.listFiles()).stream().flatMap(f -> getEntries(f).stream())
				.collect(Collectors.toList());
		if (entries.isEmpty())
			entries.add(file.getAbsolutePath() + File.separator);
		return entries;
	}

}
