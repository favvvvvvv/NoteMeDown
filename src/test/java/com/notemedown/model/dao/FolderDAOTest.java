package com.notemedown.model.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.notemedown.model.Folder;
import com.notemedown.model.Group;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:data-access-config.xml"})
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class FolderDAOTest {
	private FolderDAO dao;
	private GroupDAO groupDao;
	
	@Autowired @Required
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void setDao(FolderDAO dao) {
		this.dao = dao;
	}
	
	@Autowired @Required
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public void setGroupDao(GroupDAO groupDao) {
		this.groupDao = groupDao;
	}
	
	private Group parentGroup;
	
	@Before
	public void setUp() {
		parentGroup = new Group("Parent group");
		groupDao.save(parentGroup); 
	}
	
	@Test
	public void successWhenStoredAndRetrieved() {
		Folder folder = new Folder("My folder", parentGroup);
		long id = dao.save(folder);

		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		Folder retrievedFolder = dao.get(id);
		assertEquals(folder, retrievedFolder);
		assertNotSame(folder, retrievedFolder);
	}
	
	@Test
	public void successWhenStoredUpdatedAndRetrieved() {
		Folder folder = new Folder("Folder name", parentGroup);
		long id = dao.save(folder);
		
		String alteredName = "Altered folder name";
		folder.setName(alteredName);
		dao.update(folder);
		
		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		Folder retrievedFolder = dao.get(id);
		assertEquals(folder, retrievedFolder);
		assertNotSame(folder, retrievedFolder);
	}
	
	@Test
	public void failWhenRetrievingDeletedFolder() {
		Folder folder = new Folder("Folder to be deleted", parentGroup);
		long id = dao.save(folder);
		
		Folder retrievedFolder = dao.get(id);
		assertNotNull(retrievedFolder);
		
		dao.delete(folder);
		retrievedFolder = dao.get(id);
		assertNull(retrievedFolder);
	}
	
	private Comparator<Folder> idComparator =
			(a, b) -> a.getId() < b.getId() ? -1
					: a.getId() > b.getId() ? 1 : 0;
	
	@Test
	public void successWhenRetrievingMultipleFoldersByGroupId() {
		Group anotherParentGroup = new Group("Another parent group");
		groupDao.save(anotherParentGroup);
		
		List<Folder> parentGroupFolders = new ArrayList<>(),
				anotherParentGroupFolders = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			Folder folder = new Folder("Folder " + i, parentGroup);
			parentGroupFolders.add(folder);
			dao.save(folder);
		}
		for (int i = 3; i < 5; ++i) {
			Folder folder = new Folder("Folder " + i, anotherParentGroup);
			anotherParentGroupFolders.add(folder);
			dao.save(folder);
		}
		
		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		List<Folder> retrievedFolders = dao.getByGroup(parentGroup);
		retrievedFolders.sort(idComparator);
		assertArrayEquals(parentGroupFolders.toArray(), retrievedFolders.toArray());
		retrievedFolders = dao.getByGroup(anotherParentGroup);
		retrievedFolders.sort(idComparator);
		assertArrayEquals(anotherParentGroupFolders.toArray(), retrievedFolders.toArray());
	}
	
	@Test
	public void successWhenRetrievingMultipleFoldersByFolderId() {
		Folder parentFolder = new Folder("Parent folder", parentGroup);
		dao.save(parentFolder);
		Folder anotherParentFolder = new Folder("Another parent folder", parentGroup);
		dao.save(anotherParentFolder);
		
		List<Folder> parentFolderFolders = new ArrayList<>(),
				anotherParentFolderFolders = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			Folder folder = new Folder("Folder " + i, parentFolder);
			parentFolderFolders.add(folder);
			dao.save(folder);
		}
		for (int i = 3; i < 5; ++i) {
			Folder folder = new Folder("Folder " + i, anotherParentFolder);
			anotherParentFolderFolders.add(folder);
			dao.save(folder);
		}
		
		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		List<Folder> retrievedFolders = dao.getByFolder(parentFolder);
		retrievedFolders.sort(idComparator);
		assertArrayEquals(parentFolderFolders.toArray(), retrievedFolders.toArray());
		retrievedFolders = dao.getByFolder(anotherParentFolder);
		retrievedFolders.sort(idComparator);
		assertArrayEquals(anotherParentFolderFolders.toArray(), retrievedFolders.toArray());
	}
	
	@Test
	public void successWhenRetrievingAllFolders() {
		Folder parentFolder = new Folder("Parent folder", parentGroup);
		dao.save(parentFolder);
		Folder anotherParentFolder = new Folder("Another parent folder", parentGroup);
		dao.save(anotherParentFolder);
		
		List<Folder> parentFolderFolders = new ArrayList<>(),
				anotherParentFolderFolders = new ArrayList<>();
		for (int i = 0; i < 3; ++i) {
			Folder folder = new Folder("Folder " + i, parentFolder);
			parentFolderFolders.add(folder);
			dao.save(folder);
		}
		for (int i = 3; i < 5; ++i) {
			Folder folder = new Folder("Folder " + i, anotherParentFolder);
			anotherParentFolderFolders.add(folder);
			dao.save(folder);
		}
		
		List<Folder> allFolders = new ArrayList<Folder>(Arrays.asList(parentFolder, anotherParentFolder));
		allFolders.addAll(parentFolderFolders);
		allFolders.addAll(anotherParentFolderFolders);
		
		dao.getHibernateTemplate().flush();
		dao.getHibernateTemplate().clear();
		
		List<Folder> retrievedFolders = dao.getAll();
		retrievedFolders.sort(idComparator);
		assertArrayEquals(allFolders.toArray(), retrievedFolders.toArray());
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenStoringAnotherFolderWithSameNameIntoSameGroup() {
		Folder folder = new Folder("Folder name", parentGroup);
		dao.save(folder);
		Folder anotherFolder = new Folder("Folder name", parentGroup);
		dao.save(anotherFolder);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test
	public void successWhenStoringAnotherFolderWithSameNameIntoDifferentGroups() {
		Group anotherParentGroup = new Group("Another parent group");
		groupDao.save(anotherParentGroup);
		
		Folder folder = new Folder("Folder name", parentGroup);
		dao.save(folder);
		Folder anotherFolder = new Folder("Folder name", anotherParentGroup);
		dao.save(anotherFolder);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenStoringAnotherFolderWithSameNameIntoSameFolder() {
		Folder parentFolder = new Folder("Parent folder", parentGroup);
		dao.save(parentFolder);
		
		Folder folder = new Folder("Folder name", parentFolder);
		dao.save(folder);
		Folder anotherFolder = new Folder("Folder name", parentFolder);
		dao.save(anotherFolder);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test
	public void successWhenStoringAnotherFolderWithSameNameIntoDifferentFolders() {
		Folder parentFolder = new Folder("Parent folder", parentGroup);
		dao.save(parentFolder);
		Folder anotherParentFolder = new Folder("Another parent folder", parentGroup);
		dao.save(anotherParentFolder);
		
		Folder folder = new Folder("Folder name", parentFolder);
		dao.save(folder);
		Folder anotherFolder = new Folder("Folder name", anotherParentFolder);
		dao.save(anotherFolder);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenTryingToAddRootFolderIntoAnotherFolderInsteadOfGroup() {
		Folder anotherFolder = new Folder("Parent folder", parentGroup);
		dao.save(anotherFolder);
		Folder folder = new Folder("Not a root folder, actually", anotherFolder);
		folder.setIsRoot(true);
		dao.save(folder);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenTryingToAddNonRootFolderIntoGroupInsteadOfAnotherFolder() {
		Folder folder = new Folder("A root folder, actually", parentGroup);
		folder.setIsRoot(false);
		dao.save(folder);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenBothParentFolderAndParentGroupAreSetOnRootFolder() {
		Folder rootFolder = new Folder("Root folder", parentGroup);
		dao.save(rootFolder);
		Folder anotherRootFolder = new Folder("Another root folder", parentGroup);
		anotherRootFolder.setParentFolder(rootFolder);
		dao.save(anotherRootFolder);
		
		dao.getHibernateTemplate().flush();
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void failWhenBothParentFolderAndParentGroupAreSetOnNonRootFolder() {
		Folder rootFolder = new Folder("Root folder", parentGroup);
		dao.save(rootFolder);
		Folder nonRootFolder = new Folder("Not a root folder", rootFolder);
		nonRootFolder.setParentGroup(parentGroup);
		dao.save(nonRootFolder);
		
		dao.getHibernateTemplate().flush();
	}
}




















