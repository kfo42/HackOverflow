"use strict";

var Engine = Matter.Engine,
    Render = Matter.Render,
    World = Matter.World,
    Mouse = Matter.Mouse,
    Bodies = Matter.Bodies,
    Common = Matter.Common,
    Vertices = Matter.Vertices,
    Svg = Matter.Svg,
    Constraint = Matter.Constraint,
    Composites = Matter.Composites,
    MouseConstraint = Matter.MouseConstraint;

// create an engine
var engine = Engine.create();
var idRAF = null;
function init() {
  var numm = Math.random();
  $("canvas").remove();

  cancelAnimationFrame(idRAF);
  var width = $(window).width();
  var height = $(window).height();
  var offset = -1;
  // module aliases

  engine.events = {};
  World.clear(engine.world);
  Engine.clear(engine);

  engine = Engine.create();

  engine.world.gravity.x = 0;
  engine.world.gravity.y = 0;
  var mouseConstraint = MouseConstraint.create(engine);
  World.add(engine.world, mouseConstraint);

  // create a renderer
  var render = Render.create({
    element: document.body,
    engine: engine,
    options: {
      wireframes: false,
      background: 'transparent',
      width: width,
      height: height,
      showDebug: false,
      showBroadphase: false,
      showBounds: false,
      showVelocity: false,
      showCollisions: false,
      showSeparations: false,
      showAxes: false,
      showPositions: false,
      showAngleIndicator: false,
      showIds: false,
      showShadows: false,
      showVertexNumbers: false,
      showConvexHulls: false,
      showInternalEdges: false,
      showMousePosition: false
    }
  });

  // create two boxes and a ground
  // add all of the bodies to the world
  World.add(engine.world, [Bodies.rectangle(width / 2, height / 2.5, .5 * width, 46, {
    isStatic: true,
    render: {
      fillStyle: "blue"
    }
  }), Bodies.rectangle(width * .3 , 1 / 3 * height, 40, 260, {
    isStatic: true,
    render: {
      fillStyle: "boue"
    }
  }), Bodies.rectangle(width * .7 , 1 / 3 * height, 40, 260, {
    isStatic: true,
    render: {
      fillStyle: "blue"
    }
  }), Bodies.rectangle(width / 2, height / 2 + 40, 180, 20, {
    isStatic: true,
    render: {
      fillStyle: "transparent"
    }
  }), Bodies.rectangle(width / 2, height - offset, width, 1, {
    isStatic: true,
    render: {
      fillStyle: "#FFFFFF"
    }
  }), Bodies.rectangle(width / 2, offset, width, 1, {
    isStatic: true,
    render: {
      fillStyle: "#FFFFFF"
    }
  }), Bodies.rectangle(offset, height / 2, 1, height, {
    isStatic: true,
    render: {
      fillStyle: "#FFFFFF"
    }
  }), Bodies.rectangle(width - offset, height / 2, 1, height, {
    isStatic: true,
    render: {
      fillStyle: "#FFFFFF"
    }
  })]);

  setInterval(function(){
	if (localStorage.getItem("lefttoggle") == "true") {
		var radius = 3 + Math.random() * 20; 
		World.add(engine.world, Bodies.circle(Math.random() * width / 10 + width/2 - 200, 40 + Math.random() * 100, radius, {
		  render: {
			fillStyle: ["#4240F4", "#2A43A5", "#2B5C95", "#0228D3"][Math.round(Math.random() * 3)]
		  }

		}));
	}
	
	if (localStorage.getItem("righttoggle") == "true") {
		var radius = 3 + Math.random() * 20; 
		World.add(engine.world, Bodies.circle(Math.random() * width / 10 + width/2 + 200, 40 + Math.random() * 100, radius, {
		  render: {
			fillStyle: ["#D24034", "#CA4325", "#BB5C55", "#A22883"][Math.round(Math.random() * 3)]
		  }

		}));
	}
  }, 50);

  // run the engine
  Engine.run(engine);

  // run the renderer
  Render.run(render);

  var inc = 0;

  engine.world.gravity.y = 4;
  function update() {
    if (inc > 8) {
      // engine.world.gravity.x = Math.cos(inc / 55)/2;
    }
    inc++;
    idRAF = requestAnimationFrame(update.bind(this));
  }

  update();
}
init();

function debounce(func, wait, immediate) {
  var timeout;
  return function () {
    var context = this,
        args = arguments;
    var later = function later() {
      timeout = null;
      if (!immediate) func.apply(context, args);
    };
    var callNow = immediate && !timeout;
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
    if (callNow) func.apply(context, args);
  };
};

$(window).on("resize", debounce(function () {
  init();
}.bind(undefined), 200));