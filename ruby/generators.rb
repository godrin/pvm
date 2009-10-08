Code=Struct.new(:before,:within,:after)

class Code
  def combined
    before+within+after
  end

  def to_s
    #pp caller
    [before,within,after].flatten.join("")
  end

  def to_a
    ([before]+[within]+[after]).flatten
  end

  def indent
    self.before=self.before.map{|a|"  "+a}
    self.within=self.within.map{|a|"  "+a}
    self.after=self.after.map{|a|"  "+a}
    self
  end
end

class ASTCode
  attr_accessor :compiler
  def s(where,*args)
    args.flatten!

    args=args.map{|a|
      if a.is_a?(Code)
        [a.before,a.within,a.after]
      else
        a
      end
    }
    args.flatten!

    raise "emty !! " if args==["\n"]

    args=args.map{|a|
      if a==:ass
        "="
      elsif a.is_a?(Symbol)
        if a.to_s[0..0]=='_'
          @vars[a]||=tmpVar
        else
          a
        end
      elsif a.is_a?(String)
        a
      elsif a.is_a?(ASTCode)

      elsif a.respond_to?(:getCode)
        a.getCode
      else
        raise "#{args.inspect} is not ok"
      end
    }
    @code[where]||=[]
    @code[where]<<args.join(" ")
  end

  def b(*args)
    s(:before,*(args+["\n"]))
  end

  def w(*args)
    s(:within,*(args+["\n"]))
  end

  def as(*args)
    @code[:after]+=args.flatten.map{|a|a.to_a}.flatten
  end

  def ws(*args)
    @code[:within]+=args.flatten.map{|a|a.to_a}.flatten
  end

  def bs(*args)
    @code[:before]+=args.flatten.map{|a|a.to_a}.flatten
  end

  def a(*args)
    s(:after,*(args+["\n"]))
  end

  def compile(*a)
    @compiler.compile(*a)
  end

  def tmpVar
    @compiler.tmpVar
  end

  def append(code)
    assert{code.is_a?(Code)}
    [:before,:within,:after].each{|c|
      @code[c]||=[]
      @code[c]+=code[c]
    }
  end

  def getCode
    @vars={}
    @code={:before=>[],
      :within=>[],
      :after=>[]}

    c=code

    Code.new(@code[:before],@code[:within],@code[:after])
  end
end

class IfCode < ASTCode
  def initialize(cond,pthen,pelse)
    @cond=cond
    @then=pthen
    @else=pelse
  end

  def code
    b :_then, :ass , @then if @then
    b :_else, :ass , @else if @else
    w :_v,:ass , @cond
    w :if, :_v, :then, :_then if @then
    w :unless, :_v, :then, :_else if @else
    a :clear, :_v
    a :clear, :_then if @then
    a :clear, :_else if @else
  end
end

class BlockCode < ASTCode
  def initialize(*argss)
    @argss=argss
  end

  def code
    result=nil
    @argss.map{|as|
      all=[compile(as)].flatten.map{|a|a.indent}.flatten
      ws all
      result=all[-1].after if all[-1] and not all[-1].after=~/ *clear .*/
    }
  end
end

class SCode < ASTCode
  def initialize(*argss)
    @argss=argss
  end

  def code
    w compile(*@argss)
  end
end

class DefnCode< ASTCode
  def initialize(name,args,body)
    @name=name
    @args=args
    @body=body

  end

  def code
    w(:_func, :ass, "begin", "(",*(compile(@args)+[")"]))
   # pp "BOOODY",@body
    compile(@body).combined.each{|s|
    #  pp "COMBINDES",s
      ws *s
    }

    w "end"
    w @name, :ass, :_func
    w "clear",:_func
  end
end

class ScopeCode < ASTCode
  def initialize(*args)
    @args=args
  end

  def code
    ws compile(*@args)
  end
end

class StrCode < ASTCode
  def initialize(str)
    @str=str
  end

  def code
    b :_tmp,:ass,"\""+@str.gsub("\\","\\\\").gsub("\"","\\")+"\""

  end
end

class CallCode<ASTCode
  def initialize(*args)
    @args=args
  end

  def code
    argss=@args
    l=compile(argss[0])
    func=argss[1].to_s
    res2=compile(argss[2])

    args=res2.within
    before=res2.before
    after=res2.after
    if l
      before=l.before+before
      after=after+l.after
      b before
      w l.within[0],".",func,"(",args,")"
      a after
    else
      bs before #if before.length>0
      w func,"(",args,")"
      as after
    end
  end
end

class LvarCode<ASTCode
  def initialize(name)
    @name=name
  end

  def code
    ws @name.to_s
  end
end

class ArgsCode<ASTCode
  def initialize(*args)
    @args=args
  end

  def getCode
    @args.map{|a|[a.to_s,',']}.flatten[0...-1]
  end
end

class LitCode<ASTCode
  def initialize(lit)
    @lit=lit
  end

  def code
    ws @lit.to_s
  end
end

class ArglistCode<ASTCode
  def initialize(*args)
    @args=args
  end

  def code
    first=true
    @args.each{|a|
      t=tmpVar
      res=compile(a)
      b res.before,t,"=",res.within
      if first
        first=false
      else
        ws ','
      end
      ws t

      
      a res.after if res.after.length>0
      a "clear",t
    }
  end
end