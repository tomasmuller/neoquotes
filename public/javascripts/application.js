// NeoQuotes Application Graph Renderer
var Renderer = function(elt){
  
  var dom = $(elt)
  var canvas = dom.get(0)
  var ctx = canvas.getContext("2d");
  var gfx = arbor.Graphics(canvas)
  var sys = null

  var appCanvas = {
    
    init:function(pSystem){
      sys = pSystem
      sys.screen({size:{width:dom.width(), height:dom.height()}, padding:[40,60,40,60]})
      $(window).resize(appCanvas.resize)
      appCanvas.resize()
      // set up some event handlers to allow for node-dragging
      appCanvas.initMouseHandling()
    },
    
    resize:function(){
      canvas.width = $(window).width()
      canvas.height = .90* $(window).height()
      sys.screen({size:{width:canvas.width, height:canvas.height}})
      appCanvas.redraw()
    },
    
    redraw:function(){
      gfx.clear()
      sys.eachEdge(function(edge, p1, p2){
        if (edge.source.data.alpha * edge.target.data.alpha == 0) return
        gfx.line(p1, p2, {stroke:"#b2b19d", width:2, alpha:edge.target.data.alpha})
      })
      sys.eachNode(function(node, pt){
        var w = Math.max(20, 20+gfx.textWidth(node.name) )
        if (node.data.alpha===0) return
        if (node.data.shape=='dot'){
          gfx.oval(pt.x-w/2, pt.y-w/2, w, w, {fill:node.data.color, alpha:node.data.alpha})
          gfx.text(node.name, pt.x, pt.y+7, {color:"white", align:"center", font:"Arial", size:12})
          gfx.text(node.name, pt.x, pt.y+7, {color:"white", align:"center", font:"Arial", size:12})
        }else{
          gfx.rect(pt.x-w/2, pt.y-8, w, 20, 4, {fill:node.data.color, alpha:node.data.alpha})
          gfx.text(node.name, pt.x, pt.y+9, {color:"white", align:"center", font:"Arial", size:12})
          gfx.text(node.name, pt.x, pt.y+9, {color:"white", align:"center", font:"Arial", size:12})
        }
      })
    },
    
    initMouseHandling:function(){
      // no-nonsense drag and drop (thanks springy.js)
      var dragged = null;

      // set up a handler object that will initially listen for mousedowns then
      // for moves and mouseups while dragging
      var handler = {
        clicked:function(e){
          var pos = $(canvas).offset();
          _mouseP = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)
          dragged = sys.nearest(_mouseP);

          if (dragged && dragged.node !== null){
            // while we're dragging, don't let physics move the node
            dragged.node.fixed = true
          }

          $(canvas).bind('mousemove', handler.dragged)
          $(window).bind('mouseup', handler.dropped)

          return false
        },
        dragged:function(e){
          var pos = $(canvas).offset();
          var s = arbor.Point(e.pageX-pos.left, e.pageY-pos.top)

          if (dragged && dragged.node !== null){
            var p = sys.fromScreen(s)
            dragged.node.p = p
          }

          return false
        },
        dropped:function(e){
          if (dragged===null || dragged.node===undefined) return
          if (dragged.node !== null) dragged.node.fixed = false
          dragged.node.tempMass = 1000
          dragged = null
          $(canvas).unbind('mousemove', handler.dragged)
          $(window).unbind('mouseup', handler.dropped)
          _mouseP = null
          return false
        }
      }
      
      // start listening
      $(canvas).mousedown(handler.clicked);
    },
  }

  return appCanvas
}

// Init arbor.js
var sys = arbor.ParticleSystem();
sys.parameters({stiffness:900, repulsion:2000, gravity:true, dt:0.015});
sys.renderer = Renderer("#canvasgraph");

// Handle symbol lookup form
$('#symbol_lookup_form').submit(function(e){
  var reg = new RegExp(/^[A-Za-z0-9.]*[A-Za-z0-9][A-Za-z0-9.]*$/);
  var s = $("#symbol").val();
  if (!reg.test(s)) {
    $('#alertSymbolLookupModal').modal();
    $("#symbol").focus();
  } else {
    $.ajax({
      type: "POST",
      url: '/' + s,
      beforeSend: function(){
        $('#loadingModal').modal('show');
      },
      success: function(msg){
        if (msg == "SYMBOL_NOT_FOUND") {
          $('#alertSymbolLookupModal').modal();
          $("#symbol").focus();
        }
        else {
          // remove all nodes and edges
          sys.prune(function(node, from, to){
            return true;
          });

          // parse results to show in arbor.js
          var obj = jQuery.parseJSON(msg);

          // add nodes to graph
          $.each(obj.nodes, function(key, val){
            sys.addNode(key, val);
          });

          // set edges          
          $.each(obj.edges, function(source, val){
            $.each(val, function(target, val){
              sys.addEdge(source, target, val);
            });
          });

          // Set symbol information...
          $('#symbolInfoTitle').text(obj.company);
          $('#symbolPrice').text(obj.price);
          $('#symbolName').text(obj.symbol);

          // and show in modal dialog.
          $('#loadingModal').modal('hide');
          $('#symbolInfoModal').modal();
        }
      },
      complete: function(){
        $('#loadingModal').modal('hide');
      }
    });
  }
  e.preventDefault();
});
