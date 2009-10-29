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
    s("#{@t}=begin(#{@args.value})")+

    @body.code.indent+
    s("end")+
    @args.after+
    s("#{@klass.value}:#{@name}=#{@t}")+
    s("clear #{@t}")

  end
end