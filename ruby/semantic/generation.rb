class Base
  @@tmpVarC=0
  def prep
  end

  def method_missing(name,*args)
    puts "Not implemented #{self.class}.#{name}(#{args.inspect})"
    []
  end

  def init
  end

  def tmpVar
    @@tmpVarC+=1
    "tmp#{@@tmpVarC}"
  end

  def curModule
    parent.curModule
  end
  
  def s(str)
    [str+(" "*(80-str.length))+"#"+filename+":#{line}"]
  end
end

Dir[File.expand_path('../generation/*',__FILE__)].each{|f|
  pp f
  if f=~/^.*\.rb$/
    require f
  end
}

class Array
  def indent
    map{|l|"  "+l}
  end
end