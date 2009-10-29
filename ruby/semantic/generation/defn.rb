# normal member function
class DefnCode
  def init
    #pp self
    #exit
    @t=tmpVar
  end

  def code
    []
  end

  def before
    b=@args.before
    as=@args.value
    b+s("#{@t}=begin(#{as})")+
    @body.code.indent+s("end")+
    s("#{curModule}.#{@name}=#{@t}")
  end

  def after
    s("clear #{@t}")
  end
end