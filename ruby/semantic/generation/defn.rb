# normal member function
class DefnCode
  def init
    #pp self
    #exit
    @t=tmpVar
  end

  def before
    []
  end

  def code
    b=@args.before
    as=@args.value.gsub("&","")
    b+
    bgnws(@t,[as],@body.code,@body.value)+  
    s("#{curModule}.#{@name}=#{@t}")
  end

  def after
    s("clear #{@t}")
  end
end