/*
 * Ext JS Library 3.0 RC2
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

/**
 * @class Ext.layout.GridLayout
 * @extends Ext.layout.ContainerLayout
 * <p>This is a layout similar as WPF GridLayout</p>
 * 
 * 
 *
  
  layoutConfig: {
  	rowDefinitions: [{height: 'Auto'},{height: 'Auto'},{height: '*'},{height: '28'}],
  	columnDefinitions: [{width: 'Auto'},{width: '200'}]
  },
  items: [{
  	xtype: 'button',
  	text: 'Cell 1 1',
  	grid: { row: 0, col: 0 }
  }, {
  	xtype: 'button',
  	text: 'Cell 1 2',
  	grid: { row: 0, col: 1 }
  }, {
  	xtype: 'button',
  	text: 'Cell Row 1 col 0 ',
  	grid: { row: 1, col: 0 }
  }, {
  	xtype: 'button',
  	text: ' col 1 ',
  	grid: { row: 1, col: 1 }
  }, {
  	xtype: 'button',
  	text: ' r2c0 ',
  	grid: { row: 2, col: 0 }
  }, {
  	xtype: 'button',
  	text: ' r2c1 ',
  	grid: { row: 2, col: 1 }
  }, {
  	xtype: 'button',
  	text: ' r3c0 ',
  	grid: { row: 3, col: 0 }
  }, {
  	xtype: 'button',
  	text: ' r3c1 ',
  	grid: { row: 3, col: 1 }
  }]
  
  <Grid> 
    <Grid.RowDefinitions>
        <RowDefinition Height="Auto" />
        <RowDefinition Height="Auto" />
        <RowDefinition Height="*" />
        <RowDefinition Height="28" />
    </Grid.RowDefinitions>
    <Grid.ColumnDefinitions>
        <ColumnDefinition Width="Auto" />
        <ColumnDefinition Width="200" />
    </Grid.ColumnDefinitions>
    <Label Grid.Row="0" Grid.Column="0" Content="Name:"/>
    <Label Grid.Row="1" Grid.Column="0" Content="E-Mail:"/>
    <Label Grid.Row="2" Grid.Column="0" Content="Comment:"/>
    <TextBox Grid.Column="1" Grid.Row="0" Margin="3" />
    <TextBox Grid.Column="1" Grid.Row="1" Margin="3" />
    <TextBox Grid.Column="1" Grid.Row="2" Margin="3" />
    <Button Grid.Column="1" Grid.Row="3" HorizontalAlignment="Right" 
            MinWidth="80" Margin="3" Content="Send"  />
</Grid>
 */
Ext.layout.GridLayout = Ext.extend(Ext.layout.ContainerLayout, {
    /**
     * @cfg {String} anchor
     * <p>This configuation option is to be applied to <b>child <tt>items</tt></b> of a container managed by
     * this layout (ie. configured with <tt>layout:'anchor'</tt>).</p><br/>
     * 
     * <p>This value is what tells the layout how an item should be anchored to the container. <tt>items</tt>
     * added to an GridLayout accept an anchoring-specific config property of <b>anchor</b> which is a string
     * containing two values: the horizontal anchor value and the vertical anchor value (for example, '100% 50%').
     * The following types of anchor values are supported:<div class="mdetail-params"><ul>
     * 
     * <li><b>Percentage</b> : Any value between 1 and 100, expressed as a percentage.<div class="sub-desc">
     * The first anchor is the percentage width that the item should take up within the container, and the
     * second is the percentage height.  For example:<pre><code>
// two values specified
anchor: '100% 50%' // render item complete width of the container and 1/2 its height.
// one value specified
anchor: '100%'     // the width value; the height will default to auto
     * </code></pre></div></li>
     * 
     * <li><b>Offsets</b> : Any positive or negative integer value.<div class="sub-desc">
     * This is a raw adjustment where the first anchor is the offset from the right edge of the container,
     * and the second is the offset from the bottom edge. For example:<pre><code>
// two values specified
anchor: '-50 -100' // render item the complete width of the container minus 50 pixels and
                   // the complete height minus 100 pixels.
// one value specified
anchor: '-50'      // anchor value is assumed to be the right offset value
                   // bottom offset will default to 0
     * </code></pre></div></li>
     * 
     * <li><b>Sides</b> : Valid values are <tt>'right'</tt> (or <tt>'r'</tt>) and <tt>'bottom'</tt>
     * (or <tt>'b'</tt>).<div class="sub-desc">
     * Either the container must have a fixed size or an anchorSize config value defined at render time in
     * order for these to have any effect.</div></li>
     *
     * <li><b>Mixed</b> : <div class="sub-desc">
     * Anchor values can also be mixed as needed.  For example, to render the width offset from the container
     * right edge by 50 pixels and 75% of the container's height use:
     * <pre><code>
anchor: '-50 75%' 
     * </code></pre></div></li>
     * 
     * 
     * </ul></div>
     */
    
    // private
    monitorResize:true,

    // private
    getGridViewSize : function(ct, target){
        return target.dom == document.body ?
                   target.getViewSize() : target.getStyleSize();
    },

    // private
    onLayout : function(ct, target){
        Ext.layout.GridLayout.superclass.onLayout.call(this, ct, target);
var rds = this.rowDefinitions;
var cds = this.columnDefinitions;

        var viewSize = this.getGridViewSize(ct, target);

//        var w = size.width, h = size.height;

//        if(w < 20 && h < 20){
//            return;
//        }
        
//        console.log(size);
        var componenets = [];
        var cs = ct.items.items, len = cs.length, i, c, a, cw, ch;
        for(i = 0; i < len; i++){
        	c = cs[i];
        		//console.log(, c.grid);
        		if(!c.prefSize) {
        			c.el.dom.style.position='absolute';
        			var size = c.getSize();
        			c.prefSize = size;
        		}
        		
        		if(!componenets[c.grid.col])
        			componenets[c.grid.col]=[];
        		componenets[c.grid.col][c.grid.row] = c;
        		
        		if(!rds[c.grid.row].prefHeight) {
        			rds[c.grid.row].prefHeight=0;
        		}
        		if(c.prefSize.height > rds[c.grid.row].prefHeight) {
        			rds[c.grid.row].prefHeight = c.prefSize.height;
        		}
        		
        		if(!cds[c.grid.col].prefWidth) {
        			cds[c.grid.col].prefWidth=0;
        		}
        		if(c.prefSize.width > cds[c.grid.col].prefWidth) {
        			cds[c.grid.col].prefWidth = c.prefSize.width; 
        		}
        }
        //console.log(rds);
        //console.log(cds);
        //return;
        var rdsStars = 0;
        var fixedHeight = 0;
        for(i=0; i<rds.length; i++) {
        	var a = ""+rds[i].height;
        	if(a.indexOf("*") != -1) rds[i].star=true;
        	if(rds[i].star) {
        		rds[i].numStars = Number(a.replace("*","")) || 1;
        		rdsStars += rds[i].numStars; 
        	} else if(rds[i].height=='Auto'){
        		fixedHeight += rds[i].prefHeight; 
        	} else {
        		fixedHeight += Number(rds[i].height);
        	}
        }
        var cdsStars = 0;
        var fixedWidth = 0;
        for(i=0; i<cds.length; i++) {
        	var a = ""+cds[i].width;
        	if(a.indexOf("*") != -1) cds[i].star=true;
        	if(cds[i].star) {
        		cds[i].numStars = Number(a.replace("*","")) || 1;
        		cdsStars += cds[i].numStars;
        	} else if(cds[i].width=='Auto') {
        		fixedWidth += cds[i].prefWidth;
        	} else {
        		fixedWidth += Number(cds[i].width);
        	}
        }
        
        var rdsStarValue = rdsStars ? (viewSize.height-fixedHeight)/rdsStars : 0;
        var cdsStarValue = cdsStars ? (viewSize.width-fixedWidth)/cdsStars : 0;
        
       console.log("stars", rdsStarValue, cdsStarValue);
        for(i=0; i<componenets.length; i++) {
        	var row = componenets[i];
        	for(var j = 0; j<row.length; j++) {
        		var c = row[j];
        		cds[i].cWidth = Number(cds[i].star ? cds[i].numStars*cdsStarValue : (cds[i].width=='Auto'?cds[i].prefWidth:cds[i].width));
        		rds[j].cHeight = Number(rds[j].star ? rds[j].numStars*rdsStarValue : (rds[j].height=='Auto'?rds[j].prefHeight:rds[j].height));
        		//cds[i].cWidth = Number(cds[i].star ? cds[i].numStars*cdsStarValue : (cds[i].width=='Auto'?c.prefSize.width:cds[i].width));
        		//rds[j].cHeight = Number(rds[j].star ? rds[j].numStars*rdsStarValue : (rds[j].height=='Auto'?c.prefSize.height:rds[j].height));
        		console.log("size", i, j , cds[i].cWidth, rds[j].cHeight )
        		var margin = (this.margin||0)*2;
        		c.setSize(cds[i].cWidth - margin, rds[j].cHeight-margin);
        		var pos = {x:0,y:0}; 
        		for(var ai=0; ai<i; ai++) pos.x+=cds[ai].cWidth;
        		for(var aj=0; aj<j; aj++) pos.y+=rds[aj].cHeight;
        		console.log("POS:" + i + " " + j, pos);
        		c.setPosition(pos.x, pos.y);
        	}
        }
        //console.log(rds);
        //console.log(cds);
    },

    // private
    parseAnchor : function(a, start, cstart){
        if(a && a != 'none'){
            var last;
            if(/^(r|right|b|bottom)$/i.test(a)){   // standard anchor
                var diff = cstart - start;
                return function(v){
                    if(v !== last){
                        last = v;
                        return v - diff;
                    }
                }
            }else if(a.indexOf('%') != -1){
                var ratio = parseFloat(a.replace('%', ''))*.01;   // percentage
                return function(v){
                    if(v !== last){
                        last = v;
                        return Math.floor(v*ratio);
                    }
                }
            }else{
                a = parseInt(a, 10);
                if(!isNaN(a)){                            // simple offset adjustment
                    return function(v){
                        if(v !== last){
                            last = v;
                            return v + a;
                        }
                    }
                }
            }
        }
        return false;
    },

    // private
    adjustWidthAnchor : function(value, comp){
        return value;
    },

    // private
    adjustHeightAnchor : function(value, comp){
        return value;
    }
    
    /**
     * @property activeItem
     * @hide
     */
});
Ext.Container.LAYOUTS['grid'] = Ext.layout.GridLayout;