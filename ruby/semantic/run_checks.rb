require 'pp'
require 'compiler.rb'

def loadFile(fn)
  File.open(fn){|f|
    f.read
  }
end

def getRuby(data)
  data.split("\n").select{|line|not line=~/.*#.*/}.join("\n")
end

def getPVM(data)
  data.split("\n").select{|line|line=~/.*#.*/}.map{|line|line.sub(/.*# /,'')}.join("\n")
end

processor = RubyParser.new

compiler=Compiler.new

p=File.expand_path('../checks/*.rb',__FILE__)
Dir[p].each{|f|
  Base.clear
  file=loadFile(f)
  ruby=getRuby(file)
  pvm=getPVM(file)

  tree=processor.parse(ruby)
  
  pp tree
  result=compiler.compile(nil,tree,f).code
  lefts=pvm.split("\n").map{|l|l.chomp}
  rights=result.map{|l|l.chomp}

  i=0
  loop do
    left=lefts[i]
    right=rights[i]
    
    if (left.nil? and right.nil?)
      break 
    end
    left||=""
    right||=""
    right,*info=right.split("#")
    right||=""
    info||=[]
      info=info.join("#")
    
    right=right.rstrip
    ok=(right==left)
    if ok
      print "[OK]   "
    else
      print "[FAIL] "
    end
    max=40
    print left+" "*(max-left.length)+right+" "*(max-right.length)+info
    puts

    i+=1
  end

}
