Ext.onReady(function(){

    // create the data store
    store = new Ext.data.JsonStore({
        fields: [
           {name: 'id'},
           {name: 'name'},
           {name: 'introText'}
        ]
    });
    
    // create the Grid
    var grid = new Ext.grid.GridPanel({
        store: store,
        columns: [
            {header: 'Name', width: 160, dataIndex: 'name', renderer: function(v) { return '<b>'+v+'</b>'}},
            {header: 'Tools', width: 45, dataIndex: 'id', renderer: function(v) { return '<div id="'+v+'"></div>'}}
        ],
        stripeRows: true,
        height: 300,
        width: 380,
     // customize view config
        viewConfig: {
            forceFit:true,
            enableRowBody:true,
            showPreview:true,
            getRowClass : function(record, rowIndex, p, store){
                if(this.showPreview){
                    p.body = '<p>'+record.data.introText+'</p>';
                    return 'x-grid3-row-expanded';
                }
                return 'x-grid3-row-collapsed';
            }
        },

        //title: 'Array Grid',
        // config options for stateful behavior
        //stateful: true,
        //stateId: 'grid'        
    });
    
    // render the grid to the specified div in the page
    grid.render('content-grid');
});