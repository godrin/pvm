class Base
  @@tmpVarC=0
  def prep
  end

  def method_missing(name,*args)
    STDERR.puts "Not implemented #{self.class}.#{name}(#{args.inspect}) in #{filename}:#{line}"
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
    [str+(" "*(80-str.length))+"##"+filename+":#{line} ##"+self.class.to_s+" "+ caller[0].gsub(/.*\//,'')]
  end

  def self.clear
    @@tmpVarC=0
  end

  def bgnws(left,parameters,body,result)
    bgn(left,parameters,body,result,true)
  end

  def bgn(left,parameters,body,result,scope=false)
    if result.is_a?(String)
      assert{result=~/[a-z].*/ || result=="nil"}
    else
      pp result
      raise 1
    end
    #STDERR.puts caller.inspect unless result.is_a?(String)
    beg="begin"
    beg+="_withscope" if scope
    s("#{left}=#{beg}(#{parameters.join(", ")})")+
    (body+
    s("lreturn #{result}")).indent+
    s("end")
  end
end

Dir[File.expand_path('../generation/*',__FILE__)].each{|f|
  #pp f
  if f=~/^.*\.rb$/
    require f
  end
}

class Array
  def indent
    map{|l|"  "+l}
  end
  def select_until
    n=[]
    ok=true
    map{|e|
      ok&=!(yield e)
      if ok
        n<<e
      end
    }
    n
  end
end