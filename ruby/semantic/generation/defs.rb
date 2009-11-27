# static member
class DefsCode
  def init
    @t=tmpVar
    #    pp self
    #    exit
  end

  def before
    []
  end

  def after
    []
  end

  def code
    
    
    @args.before+
    #s("#{@t}=begin(#{@args.value})")+
    bgnws(@t,[@args.value],@body.code,@body.value.to_s)+
    @args.after+
    s("#{@klass.value}@#{@name}=#{@t}")+
    s("clear #{@t}")
  end

  def value
    @klass.value
  end
end