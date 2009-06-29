require 'ubygems'
require 'ruby_parser.rb'
require 'pp'

class File
  def self.load(filename)
    File.open(filename) {|f|f.read}
  end
end

class String 
  def indent
    "  "+gsub("\n","\n  ")
  end
end



class Compiler

  Result=Struct.new(:before,:within,:after)

  def initialize()
    @counter=0
  end
  def tmpVar
    @counter+=1
    "tmp_#{@counter}"
  end
  
  def compile(input)
    return nil if input.nil?
    args=input.map{|x|x}
    args=["_"+args[0].to_s]+args[1..-1]
    send(*args)
  end
  
  def _block(*argss)
    argss.map{|as|
      res=compile(as)
      (res.before||'')+res.within+("\n"+res.after)
    }.join("\n")
  end
  
  def _str(argss)
    Result.new("","\"#{argss}\"","")
  end
  
  def _scope(argss)
    compile(argss)
  end
  
  def _defn(*argss)
   name=argss[0]
   v=tmpVar
   argsstr=compile(argss[1])
   blockstr=compile(argss[2])
   Result.new("","#{v}=begin(#{argsstr})\n"+blockstr.indent+"\nend\n#{name}=#{v}","clear #{v}")
  end
  
  def _args(*argss)
    argss.map{|a|a.to_s}.join(", ")
  end
  
  def _call(*argss)

    l=compile(argss[0])
    func=argss[1].to_s
    res2=compile(argss[2])
    
    args=res2.within
    before=res2.before
    after=res2.after
    if l
      before=l.before+before
      after=after+l.after
      Result.new(before,l.within+"."+func+"("+args+")",after)
    else
      Result.new(before,func+"("+args+")",after)
    end
  end
  
  def _lit(*argss)
    Result.new("",argss[0].to_s,"")
  end
  
  def _arglist(*argss)
    before=""
    after=""
    as=argss.map{|a|
      t=tmpVar
      res=compile(a)
      before+=res.before+t+"="+res.within+"\n"
      after+=res.after
      after+="clear "+t+"\n"
      t
    }.join(", ")
    Result.new(before,as,after)
  end
  
  def _lvar(*argss)
    Result.new("",argss[0].to_s,"")
  end
end

@processor = RubyParser.new
a=@processor.parse(File.load("simple_function.prb"))
pp a

puts "---"
c=Compiler.new
puts c.compile(a)