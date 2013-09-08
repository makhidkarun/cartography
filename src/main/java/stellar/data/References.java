package stellar.data;

import java.util.*;

public class References
{
    private HashSet<ProviderRecord> providers;
    private ArrayList<TableRecord> tables;

    public References()
    {
        providers = new HashSet<ProviderRecord>();

        // <provider id="canon" desc="Standard (canon) data for all systems" name="Canon"/>
        ProviderRecord  r = new ProviderRecord("canon", "Standard (canon) data for all systems", "Canon");
        providers.add (r);
    }

    /*
     * The Collections interface is for the Tables array, add/remove/update
     * make the changes to the tables list. 
     * The providers collection is done through a "providersXXX" methods
     * int size()
     * boolean isEmpty()
     * boolean contains (Object 0)
     * Iterator iterator ()
     * boolean add (Object 0)
     * boolean remove (Object 0)
     * boolean containsAll (Collection C)
     * boolean addAll (Collection C)
     * boolean removeAll (Collection C)
     * boolean retainAll (Collection C)
     * void clear ()
     */

    public int getProviderSize() { return  providers.size(); } 
    public int getTableSize() { return tables == null ? 0 : tables.size(); } 

    public boolean isProviderEmpty () { return providers.isEmpty(); }
    public boolean isTablesEmpty () { return tables==null ? true : tables.isEmpty(); }
    
    public boolean containsProvider (ProviderRecord r) { return providers.contains(r); } 
    public boolean containsTable  (TableRecord t) { return tables.contains(t); }

    public Collection<ProviderRecord> providerIterator () { return providers; }
    public Collection<TableRecord> tablesIterator() { return tables == null ? 
                                                          Collections.EMPTY_LIST : 
                                                          tables; }
    
    public void addProvider(ProviderRecord record) { providers.add(record); } 
    public void addTable (TableRecord table) 
    {
        if (tables == null) tables = new ArrayList <TableRecord>();
        tables.add (table);
        //if (tables == null) tables = new EnumMap<TableRecordKey, TableRecord>(TableRecordKey.class);
        //tables.put (table.getTableKey(), table);
        //tables.add(table.getKey(), table);
    }

    public boolean removeTable (TableRecord index) { return tables.remove(index); }
    public TableRecord removeTable (int index) { return tables.remove(index); }
    public ProviderRecord getProvider (ProviderRecord record)
    {
        for (ProviderRecord p : providers)
            if (p.equals (record)) return p;
        
        return null;
    }
    
    public ProviderRecord getProvider (String key)
    {
        for (ProviderRecord p : providers)
        {
            if (p.getKey().equals(key)) return p;
        }
        return null;
    }
    
    /**
     * Looks up and returns a table based upon the full or partial name of the
     * table. 
     * @param key full or partial name of the table
     * @return the table, or null if table is not found in this reference. 
     */
    public TableRecord getTable (String key)
    {
        if (tables == null) return null;
        for (TableRecord t : tables)
        {
            if (t.getKey().equals(key)) return t;
        }
        return null;
    }
    
    public TableRecord getTable (TableRecordKey key)
    {
        if (tables == null) return null;
        for (TableRecord t : tables)
        {
            if (t.getTableKey() == key) return t;
        }
        return null;
    }
    /**
     * Post read reference data. Called by the JiBX read XML routines. 
     * Each table and table row has a "provider" which marks the source of the
     * data so users can tell where the table data comes from to decide if is 
     * relevant to them. The provider data is stored in the provider records, 
     * and marked in the xml file as an attribute. Once the reference data has
     * been read into the References, we need to connect the provider records 
     * with the table/row provider names. (WHY?)
     */
    public void postRead ()
    {
        /*

        TableRecord table;
        TableRowRecord row;
        int index;         
        ListIterator i, j; 

        i = tables.listIterator();
        while (i.hasNext())
        {
            table = (TableRecord)i.next();
            if (!table.hasProvider())
            {
                index = providers.indexOf(table.getProviderName());
                if (index >= 0)
                    table.setProvider((ProviderRecord)providers.get(index));
            }
            j = table.iterator();
            while (j.hasNext())
            {
                row = (TableRowRecord)j.next();
                if (!row.hasProvider())
                {
                    index = providers.indexOf(row.getProviderName());
                    if (index >= 0)
                        row.setProvider((ProviderRecord)providers.get(index));
                }
            }
        }
        */
    }

}
