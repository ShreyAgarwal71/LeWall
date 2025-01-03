package com.lewall.db;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lewall.db.collections.CommentCollection;
import com.lewall.db.collections.PostCollection;
import com.lewall.db.collections.UserCollection;
import com.lewall.db.helpers.Collection;

/**
 * A Database class to manage the Collection singletons in the database. This
 * class is responsible for reading and writing user, post, and comment data to
 * and from the disk. It also provides methods to get the UserCollection, the
 * PostCollection, and the CommentCollection. The Database class is a singleton
 * class.
 *
 * @author Ates Isfendiyaroglu and Mahit Mehta
 * @version 2024-11-17
 */
public class Database {
	private static final Logger logger = LogManager.getLogger(Database.class);

	private static final ScheduledThreadPoolExecutor SCHEDULER = new ScheduledThreadPoolExecutor(3);

	private static final Object MAIN_LOCK = new Object();

	private static String userFileName = "users.txt";
	private static String postFileName = "posts.txt";
	private static String commentFileName = "comments.txt";

	private static UserCollection uc;
	private static PostCollection pc;
	private static CommentCollection cc;

	private static final AtomicBoolean HAS_BEEN_INITIALIZED = new AtomicBoolean(false);

	/**
	 * This should be called before tests to initialize the test database
	 * 
	 * @param userFile
	 * @param postFile
	 * @param commentFile
	 */
	public static void init(String userFile, String postFile, String commentFile) {
		Database.userFileName = userFile;
		Database.postFileName = postFile;
		Database.commentFileName = commentFile;

		init();
	}

	/**
	 * This should be called before calling any other methods in the Database class
	 * to initialize the database
	 */
	public static void init() {
		synchronized (MAIN_LOCK) {
			if (uc == null) {
				uc = new UserCollection(Collection.getCollectionAbsolutePath(Database.userFileName), SCHEDULER);
				pc = new PostCollection(Collection.getCollectionAbsolutePath(Database.postFileName), SCHEDULER);
				cc = new CommentCollection(Collection.getCollectionAbsolutePath(Database.commentFileName),
						SCHEDULER);
			}
		}
	}

	/**
	 * This should be called after tests to cleanup the test database
	 */
	public static void cleanup(String userFile, String postFile, String commentFile) {
		synchronized (MAIN_LOCK) {
			Path dataPath = Collection.getOSDataBasePath();

			File uf = new File(dataPath.resolve(userFile).toString());
			File pf = new File(dataPath.resolve(postFile).toString());
			File cf = new File(dataPath.resolve(commentFile).toString());

			uf.deleteOnExit();
			pf.deleteOnExit();
			cf.deleteOnExit();

			if (uc != null) {
				uc = new UserCollection(Collection.getCollectionAbsolutePath(Database.userFileName), SCHEDULER);
				pc = new PostCollection(Collection.getCollectionAbsolutePath(Database.postFileName), SCHEDULER);
				cc = new CommentCollection(Collection.getCollectionAbsolutePath(Database.commentFileName),
						SCHEDULER);
			}
		}
	}

	/**
	 * This should be called after calling any other methods in the Database class
	 */
	public Database() {
		init();

		if (HAS_BEEN_INITIALIZED.compareAndSet(false, true)) {
			Runtime.getRuntime().addShutdownHook((new Thread() {
				public void run() {
					logger.info("Saving data");
					Database.uc.save();
					Database.pc.save();
					Database.cc.save();
					logger.info("Saved Data");
				}
			}));
		}
	}

	/**
	 * Get the UserCollection
	 * 
	 * @return UserCollection
	 */
	public UserCollection getUserCollection() {
		return Database.uc;
	}

	/**
	 * Get the PostCollection
	 * 
	 * @return PostCollection
	 */
	public PostCollection getPostCollection() {
		return Database.pc;
	}

	/**
	 * Get the CommentCollection
	 * 
	 * @return CommentCollection
	 */
	public CommentCollection getCommentCollection() {
		return Database.cc;
	}
}
