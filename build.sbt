name := "scalafx"

version := "0.2"

// Older version because of eighthbridge
scalaVersion := "2.11.11"

resolvers += "bintray" at "https://dl.bintray.com/giancosta86/Hephaestus"

libraryDependencies ++= {
  Seq(
    "org.scalafx" %% "scalafx" % "8.0.144-R12",
    "org.clulab" %% "eidos" % "0.2.3-SNAPSHOT",

    // This one seems to be configured for
    // https://repo1.maven.org/maven2/org/scalafx/scalafx_2.11/8.0.92-R10/scalafx_2.11-8.0.92-R10.jar
    // which conflicts with everything else.
    "info.gianlucacosta.eighthbridge" % "eighthbridge" % "8.2",

    "org.graphstream" % "gs-algo" % "1.3",
    "org.graphstream" % "gs-core" % "1.3",
    "org.graphstream" % "gs-ui"   % "1.3",

//    "org.gephi" % "gephi-toolkit" % "0.9.2",
//    "org.gephi" % "graph-api" % "0.9.2",

    "org.jgrapht" % "jgrapht-core"   % "1.3.0",
    "org.jgrapht" % "jgrapht-demo"   % "1.3.0",
    "org.jgrapht" % "jgrapht-ext"    % "1.3.0",
    "org.jgrapht" % "jgrapht-guava"  % "1.3.0",
    "org.jgrapht" % "jgrapht-io"     % "1.3.0",
    "org.jgrapht" % "jgrapht-opt"    % "1.3.0",
    
    "org.tinyjee.jgraphx" % "jgraphx" % "3.4.1.3",

    "com.sirolf2009" % "fxgraph" % "0.0.3",

    "net.sf.jung" % "jung-api"           % "2.1.1",
    "net.sf.jung" % "jung-graph-impl"    % "2.1.1",
    "net.sf.jung" % "jung-algorithms"    % "2.1.1",
    "net.sf.jung" % "jung-io"            % "2.1.1",
    "net.sf.jung" % "jung-visualization" % "2.1.1",
    "net.sf.jung" % "jung-samples"       % "2.1.1",

    "guru.nidi" % "graphviz-java" % "0.8.3",
    "org.apache.xmlgraphics" % "batik-rasterizer" % "1.10"
  )
}