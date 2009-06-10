package stellar.data;
import stellar.io.AccessXMLFile;
import stellar.io.Resources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import org.jibx.runtime.JiBXException;

/**
 * Astrogation is the top level data structure for Stellar project. It holds two
 * <code>Reference</code> data structures (a local one, which is kept with the
 * xml file and a global one kept with the program), the collection of systems, 
 * the collection of groups, and the collection of system links. 
 * <p>
 * The methods of this class provide many of the <code>Collection</code> accessors
 * for this classes five collections.
 * @see References
 * @see TableRecord
 * @version $Id: Astrogation.java,v 1.7 2008/12/29 19:35:41 tjoneslo Exp $
 */
public class Astrogation 
{
    private References references;
    private References globalReferences;
    private ArrayList<GroupRecord> groupList;
    private ArrayList<StarSystem> systems;
    private HashSet<Links> links;
    //private MutableTreeNode root;
    private ProviderRecord defaultProvider;

    /**
     * Default Constructor.
     */
    public Astrogation()
    {
        references = new References();
    }

    /**
     * Constructor with a new local refrences data set.
     * @param ref the set of <code>References</code> w
     */
    public Astrogation (References ref) 
    {
        references = ref; 
    }
    
    /**
     * Constructor which loads the global references file 
     * @param globalRefsFile The file name of the global references xml file. 
     * @throws org.jibx.runtime.JiBXException
     * @throws java.io.IOException
     */
    public Astrogation (String globalRefsFile) throws IOException, JiBXException
    {
        references = new References();
        loadGlobalReferences (globalRefsFile);
    }

    public References getGlobalReferences () { return globalReferences; } 
    public References getLocalReferences () { return references; }
    /**
     * Gets a specific table records, checking both local and global references
     * for the requested table. The local refeerences are checked first. 
     * @return the <code>TableRecord</code>, or <code>null</code> if the table
     * isn't in either set of references.
     * @param name the name of the table to retrive.
     * @see TableRecord
     */
    public TableRecord getTableRecord(TableRecordKey name)
    {
        TableRecord table = null;
        if (references != null)
        {
            table = references.getTable(name);
        }
        if (table == null)
        {
            table = globalReferences.getTable(name);
        }
        return table;
    }
    
    public ProviderRecord getDefaultProvider () { return references.getProvider(defaultProvider); } 
    public void setProvider (ProviderRecord name) { references.addProvider(name); }
    public void setDefaultProvider (ProviderRecord record) 
    { 
        defaultProvider = record; 
    } 
    //public int getProviderIndex (ProviderRecord name) { return references.getProviderIndex(name); }
    
    public void addGroup (GroupRecord sector)
    {
        if (groupList == null) { groupList = new ArrayList<GroupRecord>(); }
        groupList.add(sector);
        setGroupData();
    }
    public GroupRecord getGroup (int index) { return groupList == null ? null : groupList.get(index); } 
    public ListIterator<GroupRecord> getGroups () { return (groupList == null ? Collections.EMPTY_LIST.listIterator() : groupList.listIterator()); } 
    public int getGroupCount () { return (groupList == null ? -1 : groupList.size()); } 
    
    public void setSystems (ArrayList newSystems) { this.systems = newSystems; }
    public void addSystemData (ArrayList newSystems) { systems.addAll (newSystems); }
    public void removeSystem (StarSystem system) { systems.remove(system); }
    public void removeSystem (int index) { systems.remove(index); } 
    public StarSystem getSystem (int index) { return (systems == null) ? null : systems.get(index); }
    public int getSystemCount () { return (systems == null) ? -1 : systems.size(); }
    public ListIterator<StarSystem> getSystems () { return (systems == null) ? Collections.EMPTY_LIST.listIterator() : systems.listIterator(); }
    public void addSystem (StarSystem system) 
    { 
        if (systems == null) { systems = new ArrayList<StarSystem>(); } 
        systems.add(system); 
    } 

    public Iterator getLinks() { return (links == null) ? Collections.EMPTY_LIST.listIterator() : links.iterator(); }
    public void addLink (Links newLink)
    {
        if (links == null) { links = new HashSet (); }
        if (links.contains(newLink)) return;
        links.add(newLink);
    }
    public int getLinkCount () { return (links == null) ? -1 : links.size(); } 

    /**
     * Recreates the key ID for the links. The XML store requires the links have
     * a unique key, so when the links are added here as converted from the older
     * file formats, they need to be assigned these unique ids. As a refactoring,
     * rather than try and keep a link id in the read file, this gives all the
     * links a unique id at once when the data has finished been read. 
     * 
     * @see com.softstart.stellar.io.SECFileReader
     */
    public void renumberLinks () 
    {
        int j = 1;
        HashSet newLinks = new HashSet (links.size());
        /* Reset all the key values to be in the order of the table. */
        for (Iterator i = links.iterator(); i.hasNext();) 
        {
            Links l = (Links)i.next();
            i.remove();
            l.setKey (TableRecordKey.LINKS.toString() + "." + Integer.toString(j));
            newLinks.add(l);
            j++;
        }
        links = newLinks;
    }
    /**
     * Gets the star system data based upon a specific hex id.
     * @return StarSystem data
     * @param xy HexID location of the system. 
     */
    public StarSystem getSystem (HexID xy)
    {
        if (systems == null) return null;
        for (int i = 0; i < systems.size(); i++)
        {
            if (((StarSystem)systems.get(i)).getLocation().equals(xy))
            return ((StarSystem)systems.get(i));
        }
        return null;
    }
    
    public GroupRecord getGroup (HexID xy, GroupType level)
    {
        if (groupList == null) return null;
        for (int i = 0; i < groupList.size(); i++)
        {
            GroupRecord g = groupList.get(i);
            if (g.getType() == level && g.getLocation().equals(xy)) return g;
        }
        return null;
    }

    /**
     * Determine if there are any groups of a specific scope within the groups
     * listing of this data. 
     * @return <code>true</code> if there is at least one group of the specified
     *      scope, <code>false</code> if not.
     * @param scope the <code>MapIcon</code> scope index.
     */
    public boolean isGroupPresent (GroupType scope)
    {
        if (groupList == null) return false;
        for (int i = 0; i < groupList.size(); i++)
        {
            GroupRecord g = groupList.get(i);
            if (g.getType() == scope) return true;
        }
        return false;
    }
    
    /**
     * Gets the root of the groups list. Some of the groups can be arranged into 
     * a tree structure (Domain > Sector > Quadrant > SubSector). This returns
     * the root of that tree. 
     * @return The top most group of the group list.
     */
    //public TreeNode getRoot() { return root; } 
    
    /**
     * After the data from a sector file has been loaded, the system need links
     * between the star systems and the group records. This allows a faster 
     * rendering of the maps when the scope has been limited. This requires
     * going through all the systems and finding the groups for the hexes, then 
     * through the parent groups and adding the star lists for the children. 
     */
    private void setGroupData ()
    {
        /*
        for (int i = 0; i < groupList.size(); i++)
        {
            GroupRecord g = (GroupRecord)groupList.get(i);
            /*
             * Look through all the groups to find the parent record of this
             * group. 
             *
            if (g.getParentName() != null) 
            {
                for (int j = 0; j < groupList.size(); j++)
                {
                    GroupRecord parent = (GroupRecord)groupList.get(j);
                    if (g.getParentName().equals (parent.getName()))
                    {
                        if (parent.getIndex(g) < 0)
                        {
                            parent.insert (g, parent.getChildCount());
                        }
                        break;
                    }
                }
            }
        }
        /*
         * find the root of the group list tree.
         *
        root = (MutableTreeNode)groupList.get(0);
        while (root.getParent() != null)
        {
            root = (MutableTreeNode)root.getParent();
        }
        /*
         * loop through the groups and find the systems in each group.
         */
        for (int i = 0; i < groupList.size(); i++)
        {
            GroupRecord g = groupList.get(i);
            /*
             * Look through all the systems to find the ones in this group
             */
            if (systems != null)
            {
                for (int j = 0; j < systems.size(); j++)
                {
                    StarSystem s = (StarSystem)systems.get(j);
                    if (g.inGroup(s.getLocation())) g.addSystem(s);
                }
            }
            /*
             * Look through all the links (if any) which are in this group
             */
            if (links != null) 
            {
                for (Iterator j = links.iterator(); j.hasNext(); )
                {
                    Links l = (Links)j.next();
                    for (Iterator k = l.getHexes(); k.hasNext(); )
                    {
                        HexID h = (HexID)k.next();
                        if (g.inGroup(h)) g.addLink(l);
                    }
                }
            }
        }
    }
    
    /**
     * This creates the link between the links (lines drawn between systems), and
     * the link group, which is stored as a local reference data table. Like the
     * group data links above, this allows us to filter links easily and set some
     * information (usually just color) for all links in the group. 
     */
    private void setLinkData()
    {
        TableRecord linkRecords;
        if (links == null || links.isEmpty()) return;
        linkRecords = references.getTable(TableRecordKey.LINKS);
        if (linkRecords == null || linkRecords.isEmpty()) return;
        for (Iterator j = links.iterator(); j.hasNext(); )
        {
            Links l = (Links)j.next();
            l.setLinkType (linkRecords.getRecord (l.getLinkTypeName()));
        }
        
    }
    /**
     * JiBX unmarsalling post processing method. JiBX automatically calls this
     * method when it has finised reading all of the Astrogation XML structure. 
     */
    public void postRead()
    {
        if (groupList != null) setGroupData();
        setLinkData();
    }
    
    public References loadGlobalReferences(String file) throws IOException, JiBXException
    {
        AccessXMLFile data;
        if (globalReferences != null) return globalReferences;
        
        if (file == null || file.length() < 3)
        {
            data = new AccessXMLFile (Resources.getReferences());
        }
        else
        {
            data =  new AccessXMLFile (file);
        }
        data.read();
        globalReferences = data.getAstrogation().getLocalReferences();
        return globalReferences;
    }
    
}
