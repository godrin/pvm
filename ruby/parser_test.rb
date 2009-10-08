require 'ubygems'
require 'ruby_parser.rb'
require 'pp'

class IO
  def self.load(filename)
    File.open(filename) {|f|f.read}
  end
end

class String
  def indent
    "  "+gsub("\n","\n  ")
  end
end

class ASTCode
  def getCode
    @code=[]
    @vars={}
  end
  
  def s(*args)
    args=args.map{|a|
      if a==:ass
        "="
      elsif a.is_a?(Symbol)
        if a.to_s[0..0]=='_'
          @vars[a]||=tmpVar
        end
      else
        a.getCode
      end
    }
    @code<<args.join(" ")
  end
  
  def getCode
    @code=[]
    c=code
    c.join("\n")
  end
end

class If < ASTCode
  def initialize(cond,pthen,pelse)
    @cond=cond
    @then=pthen
    @else=pelse
  end
  def code
    s :_v,:ass , @cond
    s :_then, :ass , @then if @then
    s :_else, :ass , @else if @else
    s :if, :_v, :then, :_then if @then
    s :unless, :_v, :then, :_else if @else
    s :clear, :_v
    s :clear, :_then if @then
    s :clear, :_else if @else
  end
end

class Block
  def initialize
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

  def _const(argss)
    Result.new("",argss.to_s,"")
  end

  def _str(argss)
    v=tmpVar
    Result.new("#{v}=\"#{argss}\"\n",v,"")
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

  def _if(*argss)
    vars=[]
    v=tmpVar
    vars<<v
    text="#{v}="+compile(argss[0])
    before=""
    if argss[1]
      v1=tmpVar
      r=compile(argss[1])
      before<<r.before<<"\n"
      text<<"#{v1}="
      text<<"\nif #{v} then #{c}"
    end
    if argss[2]
      before<<compile(argss[2])<<"\n"
      text<<"\nunless #{v} then #{c}"
    end
    varstext=vars.each{|v|"clear #{v}"}.join("\n")
    Result.new(before,text,varstext)
    #pp argss
    #exit
  end

  def _defined(arg)
    pp arg
    exit
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
c=STDIN.read

a=@processor.parse(c)
#STDERR.puts a.inspect
c=Compiler.new
puts c.compile(a)